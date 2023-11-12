package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class CampaignDetailed(
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
    @SerializedName("DelingProduct")
    val dealingProduct: String = "",
    @SerializedName("DelingProductId")
    val dealingProductId: String = "",
    @SerializedName("HasHistory")
    val hasHistory: Int = 0,
    @SerializedName("ImportedAt")
    val importedAt: String = "",
    @SerializedName("ImportedBy")
    val importedBy: String = "",
    @SerializedName("MobileNumber")
    val mobileNumber: String = "",
    @SerializedName("Product")
    val product: String = "",
    @SerializedName("ProductId")
    val productId: String = ""
)