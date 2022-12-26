package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.UserAPI
import com.nyorsi.p3track.api.queryModels.department.GetDepartmentsResponse
import retrofit2.Response

class DepartmentRepository {
    suspend fun getDepartments(token: String) : Response<MutableList<GetDepartmentsResponse>> {
        return UserAPI.getAPI().getDepartments(token = token)
    }
}