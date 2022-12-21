package com.nyorsi.p3track.repositories

import com.nyorsi.p3track.api.UserAPI
import com.nyorsi.p3track.api.queryModels.activities.GetActivitiesResponse
import retrofit2.Response

class ActivityRepository {
    suspend fun getActivities(token: String) : Response<MutableList<GetActivitiesResponse>> {
        return UserAPI.getAPI().getActivities(token = token)
    }
}