package com.nyorsi.p3track.api.queryModels.department

import com.google.gson.annotations.SerializedName

data class GetDepartmentsResponse (
    @SerializedName("ID")
    var ID: Int,
    @SerializedName("name")
    var name: String
)