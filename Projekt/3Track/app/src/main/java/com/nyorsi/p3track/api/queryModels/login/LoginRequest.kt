package com.nyorsi.p3track.api.queryModels.login

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email")
    var email: String,
    @SerializedName("passwordKey")
    var password: String
)
