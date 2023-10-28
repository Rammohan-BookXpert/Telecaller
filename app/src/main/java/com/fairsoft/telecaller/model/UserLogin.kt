package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class UserLogin(
    @SerializedName("IsBookXpertUser")
    val isBookXpertUser: Int = 0,
    @SerializedName("UserId")
    val userId: Int = 0,
    @SerializedName("UserName")
    val username: String = "",
    @SerializedName("IsLead")
    val isLead: Int = 0,
    @SerializedName("Password")
    val password: String? = "",
    @SerializedName("Role")
    val role: String = "",
    @SerializedName("State")
    val state: String = "",
    @SerializedName("Phone")
    val phone: String = "",
    @SerializedName("ISActive")
    val isActive: Boolean = true,
    @SerializedName("DeviceId")
    val deviceId: String = "",
)
