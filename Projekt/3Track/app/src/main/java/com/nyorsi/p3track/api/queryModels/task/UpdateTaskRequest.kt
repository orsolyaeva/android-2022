package com.nyorsi.p3track.api.queryModels.task

import com.google.gson.annotations.SerializedName

data class UpdateTaskRequest(
    @SerializedName("taskId")
    val taskId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("assignedToUserId")
    val assignedToUserId: Int,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("deadline")
    val deadline: Long,
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("progress")
    val progress: Int?
)
