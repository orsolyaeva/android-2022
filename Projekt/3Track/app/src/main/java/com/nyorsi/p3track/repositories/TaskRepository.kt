package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.UserAPI
import com.nyorsi.p3track.api.queryModels.GeneralResponse
import com.nyorsi.p3track.api.queryModels.task.CreateTaskRequest
import com.nyorsi.p3track.api.queryModels.task.GetTasksResponse
import com.nyorsi.p3track.api.queryModels.task.UpdateTaskRequest
import retrofit2.Response

class TaskRepository {
    suspend fun getTasks(token: String) : Response<MutableList<GetTasksResponse>> {
        return UserAPI.getAPI().getTasks(token = token)
    }

    suspend fun createTask(token: String, createTaskRequest: CreateTaskRequest): Response<GeneralResponse> {
        return UserAPI.getAPI().createTask(token = token, createTaskRequest = createTaskRequest)
    }

    suspend fun updateTask(token: String, updateTaskRequest: UpdateTaskRequest): Response<GeneralResponse> {
        return UserAPI.getAPI().updateTask(token = token, updateTaskRequest = updateTaskRequest)
    }
}