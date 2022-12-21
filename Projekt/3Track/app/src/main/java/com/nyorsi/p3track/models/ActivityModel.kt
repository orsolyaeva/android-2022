package com.nyorsi.p3track.models

enum class ActivityType {
    DEPARTMENT,
    TASK,
    ANNOUNCEMENT
}

enum class ActivitySubType {
    DEPARTMENT_USER_ADDED,
    TASK_CREATED,
    TASK_ASSIGNED,
    TASK_STATUS_CHANGE,
    TASK_PROGRESS_CHANGE
}

data class ActivityModel(
    var id: Int,
    var activityType: ActivityType,
    var activitySubType: ActivitySubType,
    var createdByUser: UserModel?,
    var createdTime: Long,
    var activityTypeSubId: Int,
    var assignedToId: UserModel?,
    var note: String?,
    var progress: Int?
)
