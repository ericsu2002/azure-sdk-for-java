// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.autoconfigure.aad;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public class UserPrincipalAzureADGraphTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9519);

    private AzureADGraphClient graphClientMock;
    private String clientId;
    private String clientSecret;
    private AADAuthenticationProperties aadAuthenticationProperties;
    private ServiceEndpointsProperties serviceEndpointsProperties;
    private String accessToken;
    private static String userGroupsJson;

    static {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final Map<String, Object> json = objectMapper.readValue(UserPrincipalAzureADGraphTest.class.getClassLoader()
                                                                                                       .getResourceAsStream("aad/azure-ad-graph-user-groups.json"),
                new TypeReference<HashMap<String, Object>>() {
                });
            userGroupsJson = objectMapper.writeValueAsString(json);
        } catch (IOException e) {
            e.printStackTrace();
            userGroupsJson = null;
        }
        Assert.assertNotNull(userGroupsJson);
    }

    @Before
    public void setup() {
        accessToken = TestConstants.ACCESS_TOKEN;
        aadAuthenticationProperties = new AADAuthenticationProperties();
        serviceEndpointsProperties = new ServiceEndpointsProperties();
        final ServiceEndpoints serviceEndpoints = new ServiceEndpoints();
        serviceEndpoints.setAadMembershipRestUri("http://localhost:9519/memberOf");
        serviceEndpointsProperties.getEndpoints().put("global", serviceEndpoints);
        clientId = "client";
        clientSecret = "pass";
    }

    @Test
    public void getGroups() throws Exception {
        aadAuthenticationProperties.getUserGroup().setAllowedGroups(Arrays.asList("group1", "group2", "group3"));
        this.graphClientMock = new AzureADGraphClient(clientId, clientSecret, aadAuthenticationProperties,
            serviceEndpointsProperties);

        stubFor(get(urlEqualTo("/memberOf"))
            .withHeader(ACCEPT, equalTo("application/json;odata=minimalmetadata"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .withBody(userGroupsJson)));

        Set<String> groups = graphClientMock.getGroups(TestConstants.ACCESS_TOKEN);
        assertThat(groups).isNotEmpty().containsExactlyInAnyOrder("group1", "group2", "group3");

        verify(getRequestedFor(urlMatching("/memberOf"))
            .withHeader(HttpHeaders.AUTHORIZATION, equalTo(String.format("Bearer %s", accessToken)))
            .withHeader(ACCEPT, equalTo("application/json;odata=minimalmetadata"))
            .withHeader("api-version", equalTo("1.6")));
    }

    @Test
    public void userPrincipalIsSerializable() throws ParseException, IOException, ClassNotFoundException {
        final File tmpOutputFile = File.createTempFile("test-user-principal", "txt");

        try (FileOutputStream fileOutputStream = new FileOutputStream(tmpOutputFile);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
             FileInputStream fileInputStream = new FileInputStream(tmpOutputFile);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            final JWSObject jwsObject = JWSObject.parse(TestConstants.JWT_TOKEN);
            final JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject("fake-subject").build();
            final UserPrincipal principal = new UserPrincipal("", jwsObject, jwtClaimsSet);

            objectOutputStream.writeObject(principal);

            final UserPrincipal serializedPrincipal = (UserPrincipal) objectInputStream.readObject();

            Assert.assertNotNull("Serialized UserPrincipal not null", serializedPrincipal);
            Assert.assertFalse("Serialized UserPrincipal kid not empty",
                StringUtils.isEmpty(serializedPrincipal.getKid()));
            Assert.assertNotNull("Serialized UserPrincipal claims not null.", serializedPrincipal.getClaims());
            Assert.assertTrue("Serialized UserPrincipal claims not empty.",
                serializedPrincipal.getClaims().size() > 0);
        } finally {
            Files.deleteIfExists(tmpOutputFile.toPath());
        }
    }
}
