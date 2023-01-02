package com.nyorsi.p3track.api.queryModels.users

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("imageUrl")
    val imageUrl: String
)
