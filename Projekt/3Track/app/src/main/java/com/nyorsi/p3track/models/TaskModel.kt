package com.nyorsi.p3track.models

enum class TaskStatus {
    OPEN,
    IN_PROGRESS,
    DONE
}

data class TaskModel(
    var id: Int,
    var title: String,
    var description: String,
    var createdTime: Long,
    var createdBy: UserModel?,
    var assignedTo: UserModel?,
    var priority: Int,
    var deadline: Long,
    var department: DepartmentModel?,
    var status: Int,
    var progress: Int?
)
