package com.nyorsi.p3track.models

enum class TaskStatus {
    NEW,
    IN_PROGRESS,
    BLOCKED,
    DONE
}

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

data class TaskModel(
    var id: Int,
    var title: String,
    var description: String,
    var createdTime: Long,
    var createdBy: UserModel?,
    var assignedTo: UserModel?,
    var priority: TaskPriority,
    var deadline: Long,
    var department: DepartmentModel?,
    var status: TaskStatus,
    var progress: Int?
)
