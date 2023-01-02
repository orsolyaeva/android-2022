package com.nyorsi.p3track.api.queryModels

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: Int?
)
