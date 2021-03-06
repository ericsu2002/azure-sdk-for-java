/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.loganalytics.v2020_08_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The daily volume cap for ingestion.
 */
public class WorkspaceCapping {
    /**
     * The workspace daily quota for ingestion.
     */
    @JsonProperty(value = "dailyQuotaGb")
    private Double dailyQuotaGb;

    /**
     * The time when the quota will be rest.
     */
    @JsonProperty(value = "quotaNextResetTime", access = JsonProperty.Access.WRITE_ONLY)
    private String quotaNextResetTime;

    /**
     * The status of data ingestion for this workspace. Possible values
     * include: 'RespectQuota', 'ForceOn', 'ForceOff', 'OverQuota',
     * 'SubscriptionSuspended', 'ApproachingQuota'.
     */
    @JsonProperty(value = "dataIngestionStatus", access = JsonProperty.Access.WRITE_ONLY)
    private DataIngestionStatus dataIngestionStatus;

    /**
     * Get the workspace daily quota for ingestion.
     *
     * @return the dailyQuotaGb value
     */
    public Double dailyQuotaGb() {
        return this.dailyQuotaGb;
    }

    /**
     * Set the workspace daily quota for ingestion.
     *
     * @param dailyQuotaGb the dailyQuotaGb value to set
     * @return the WorkspaceCapping object itself.
     */
    public WorkspaceCapping withDailyQuotaGb(Double dailyQuotaGb) {
        this.dailyQuotaGb = dailyQuotaGb;
        return this;
    }

    /**
     * Get the time when the quota will be rest.
     *
     * @return the quotaNextResetTime value
     */
    public String quotaNextResetTime() {
        return this.quotaNextResetTime;
    }

    /**
     * Get the status of data ingestion for this workspace. Possible values include: 'RespectQuota', 'ForceOn', 'ForceOff', 'OverQuota', 'SubscriptionSuspended', 'ApproachingQuota'.
     *
     * @return the dataIngestionStatus value
     */
    public DataIngestionStatus dataIngestionStatus() {
        return this.dataIngestionStatus;
    }

}
