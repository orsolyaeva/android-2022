package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.queryModels.login.LoginRequest
import com.nyorsi.p3track.api.queryModels.login.LoginResponse
import com.nyorsi.p3track.api.UserAPI
import com.nyorsi.p3track.api.queryModels.users.GetUsersResponse
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest) : Response<LoginResponse> {
        return UserAPI.getAPI().loginUser(loginRequest = loginRequest)
    }

    suspend fun getMyUser(token: String) : Response<GetUsersResponse> {
        return UserAPI.getAPI().getMyUser(token = token)
    }

    suspend fun getUsers(token: String): Response<MutableList<GetUsersResponse>> {
        return UserAPI.getAPI().getUsers(token = token)
    }
}