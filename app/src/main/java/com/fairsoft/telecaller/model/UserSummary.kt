package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class UserSummary(
    @SerializedName("UserName")
    val username: String = "",
    @SerializedName("TotalCalls")
    val totalCalls: Int = 0,
    @SerializedName("Connected")
    val connected: Int = 0,
    @SerializedName("NotConnected")
    val notConnected: Int = 0,
    @SerializedName("NumberOfEquiries")
    val noOfEnquiries: Int = 0
)
