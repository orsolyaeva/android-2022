package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.UserAPI
import com.nyorsi.p3track.api.queryModels.task.GetTasksResponse
import retrofit2.Response

class TaskRepository {
    suspend fun getTasks(token: String) : Response<MutableList<GetTasksResponse>> {
        return UserAPI.getAPI().getTasks(token = token)
    }
}