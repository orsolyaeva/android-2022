package com.nyorsi.p3track.api.queryModels.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId")
    var userId: Int,
    @SerializedName("token")
    var token: String,
    @SerializedName("deadline")
    var deadline: Long,
)
