package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class CampaignsList(
    @SerializedName("CampaignId")
    val campaignId: Int = 0,
    @SerializedName("CampaignName")
    val campaignName: String = "",
    @SerializedName("TotalNoOfCustomers")
    val totalNoOfCustomer: Int = 0,
    @SerializedName("ConnectedCustomers")
    val connectedCustomers: Int = 0,
    @SerializedName("NotConnectedCustomers")
    val notConnectedCustomers: Int = 0,
    @SerializedName("OpenCustomers")
    val openCustomer: Int = 0
)
