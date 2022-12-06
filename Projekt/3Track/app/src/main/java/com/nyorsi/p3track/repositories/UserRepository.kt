package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.login.LoginRequest
import com.nyorsi.p3track.api.login.LoginResponse
import com.nyorsi.p3track.api.login.UserAPI
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest) : Response<LoginResponse> {
        return UserAPI.getAPI().loginUser(loginRequest = loginRequest)
    }
}