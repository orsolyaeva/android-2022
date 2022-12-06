package com.nyorsi.p3track.api.login

import com.nyorsi.p3track.api.ApiClient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    companion object {
        fun getAPI(): UserAPI {
            return ApiClient.client!!.create(UserAPI::class.java)
        }
    }
}