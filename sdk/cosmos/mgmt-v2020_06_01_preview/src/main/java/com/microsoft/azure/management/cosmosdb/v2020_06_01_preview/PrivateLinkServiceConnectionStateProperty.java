/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.cosmosdb.v2020_06_01_preview;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Connection State of the Private Endpoint Connection.
 */
public class PrivateLinkServiceConnectionStateProperty {
    /**
     * The private link service connection status.
     */
    @JsonProperty(value = "status")
    private String status;

    /**
     * Any action that is required beyond basic workflow (approve/ reject/
     * disconnect).
     */
    @JsonProperty(value = "actionsRequired", access = JsonProperty.Access.WRITE_ONLY)
    private String actionsRequired;

    /**
     * The private link service connection description.
     */
    @JsonProperty(value = "description")
    private String description;

    /**
     * Get the private link service connection status.
     *
     * @return the status value
     */
    public String status() {
        return this.status;
    }

    /**
     * Set the private link service connection status.
     *
     * @param status the status value to set
     * @return the PrivateLinkServiceConnectionStateProperty object itself.
     */
    public PrivateLinkServiceConnectionStateProperty withStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get any action that is required beyond basic workflow (approve/ reject/ disconnect).
     *
     * @return the actionsRequired value
     */
    public String actionsRequired() {
        return this.actionsRequired;
    }

    /**
     * Get the private link service connection description.
     *
     * @return the description value
     */
    public String description() {
        return this.description;
    }

    /**
     * Set the private link service connection description.
     *
     * @param description the description value to set
     * @return the PrivateLinkServiceConnectionStateProperty object itself.
     */
    public PrivateLinkServiceConnectionStateProperty withDescription(String description) {
        this.description = description;
        return this;
    }

}
