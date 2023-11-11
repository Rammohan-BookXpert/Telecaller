package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class CampNotConnected(
    @SerializedName("CampaignCustomerId")
    val campaignCustomerId: Int = 0,
    @SerializedName("CampaignId")
    val campaignId: Int = 0,
    @SerializedName("CampaignName")
    val campaignName: String = "",
    @SerializedName("City")
    val city: String = "",
    @SerializedName("CompanyName")
    val companyName: String = "",
    @SerializedName("CreatedOn")
    val createdOn: String = "",
    @SerializedName("DealingProduct")
    val dealingProduct: String = "",
    @SerializedName("DealingProductId")
    val dealingProductId: String = "",
    @SerializedName("HasHistory")
    val hasHistory: Int = 0,
    @SerializedName("MobileNumber")
    val mobileNumber: String = "",
    @SerializedName("Product")
    val product: String = "",
    @SerializedName("ProductId")
    val productId: String = "",
    @SerializedName("Reason")
    val reason: String = "",
    @SerializedName("Remarks")
    val remarks: String = "",
    @SerializedName("UserName")
    val userName: String = ""
)