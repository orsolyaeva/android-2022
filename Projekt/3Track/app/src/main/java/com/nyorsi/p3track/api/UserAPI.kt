package com.nyorsi.p3track.api

import com.nyorsi.p3track.api.queryModels.activities.GetActivitiesResponse
import com.nyorsi.p3track.api.queryModels.department.GetDepartmentsResponse
import com.nyorsi.p3track.api.queryModels.login.LoginRequest
import com.nyorsi.p3track.api.queryModels.login.LoginResponse
import com.nyorsi.p3track.api.queryModels.task.CreateTaskRequest
import com.nyorsi.p3track.api.queryModels.GeneralResponse
import com.nyorsi.p3track.api.queryModels.task.GetTasksResponse
import com.nyorsi.p3track.api.queryModels.task.UpdateTaskRequest
import com.nyorsi.p3track.api.queryModels.users.GetUsersResponse
import com.nyorsi.p3track.api.queryModels.users.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
    @POST("/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/user")
    suspend fun getMyUser(
        @Header("token") token: String
    ): Response<GetUsersResponse>

    @GET("/users")
    suspend fun getUsers(
        @Header("token") token: String
    ): Response<MutableList<GetUsersResponse>>

    @GET("/activity/getActivities")
    suspend fun getActivities(
        @Header ("token") token: String,
    ): Response<MutableList<GetActivitiesResponse>>

    @GET("/department")
    suspend fun getDepartments(
        @Header("token") token: String
    ): Response<MutableList<GetDepartmentsResponse>>

    @GET("/task/getTasks")
    suspend fun getTasks(
        @Header("token") token: String
    ): Response<MutableList<GetTasksResponse>>

    @POST("task/create")
    suspend fun createTask(
        @Header("token") token: String,
        @Body createTaskRequest: CreateTaskRequest
    ): Response<GeneralResponse>

    @POST("task/update")
    suspend fun updateTask(
        @Header("token") token: String,
        @Body updateTaskRequest: UpdateTaskRequest
    ): Response<GeneralResponse>

    @POST("users/updateProfile")
    suspend fun updateProfile(
        @Header("token") token: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): Response<GeneralResponse>

    companion object {
        fun getAPI(): UserAPI {
            return ApiClient.client!!.create(UserAPI::class.java)
        }
    }
}