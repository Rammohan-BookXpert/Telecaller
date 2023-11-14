package com.fairsoft.telecaller.model

import com.google.gson.annotations.SerializedName

data class ContactHistory(
    @SerializedName("FollowupBy")
    val followUpBy: String = "",
    @SerializedName("Remarks")
    val remarks: String = "",
    @SerializedName("FEnqDate")
    val fEnqDate: String = "",
    @SerializedName("IsInterested")
    val isInterested: String = ""
)