package com.nyorsi.p3track.api.queryModels.task

import com.google.gson.annotations.SerializedName

data class GetTasksResponse(
    @SerializedName("ID")
    var ID: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("created_time")
    var created_time: Long,
    @SerializedName("created_by_user_ID")
    var created_by_user_ID: Int,
    @SerializedName("assigned_to_user_ID")
    var assigned_to_user_ID: Int,
    @SerializedName("priority")
    var priority: Int,
    @SerializedName("deadline")
    var deadline: Long,
    @SerializedName("department_ID")
    var department_ID: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("progress")
    var progress: Int?
)
