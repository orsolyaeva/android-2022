package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.models.ActivitySubType
import com.nyorsi.p3track.models.ActivityType
import com.nyorsi.p3track.models.UserModel
import com.nyorsi.p3track.repositories.ActivityRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application){
    companion object {
        val TAG: String = ActivityViewModel::class.java.simpleName
    }

    private val activityRepository = ActivityRepository()
    val getActivitiesState: MutableLiveData<RequestState> = MutableLiveData()
    var activityList : MutableLiveData<List<ActivityModel>> = MutableLiveData()
    var userList: MutableLiveData<List<UserModel>> = MutableLiveData()

    fun getActivities() {
        getActivitiesState.value = RequestState.LOADING
        viewModelScope.launch {
           try {
               val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
               val token = sharedPref.getString("token", null)
               Log.d("Token", "ActivityViewModel: " + token.toString())
               if(token != null) {
                   activityList.value = listOf()
                   val response = activityRepository.getActivities(token)
                   if(response.isSuccessful) {
                       val activities = response.body()!!
                       Log.d("Activities", userList.value.toString())
                       for(activity in activities) {
                           val user = userList.value?.find { it.id == activity.created_by_user_id }
                           val subUser = userList.value?.find { it.id == activity.sub_user_ID }
                           activityList.value  = activityList.value?.plus(ActivityModel(activity.ID,
                               ActivityType.values()[activity.type],
                               ActivitySubType.values()[activity.sub_type],
                               user, activity.created_time, activity.sub_ID,
                               subUser, activity.note, activity.progress,))
//                           Log.d("ActivityList", "getActivities: " + activityList.value.toString())
                       }
                       getActivitiesState.value = RequestState.SUCCESS
                   } else {
                          getActivitiesState.value = RequestState.UNKNOWN_ERROR
                          Log.d(TAG, "getActivities: ${response.errorBody()}")
                   }
               }
           } catch (e: Exception) {
               getActivitiesState.value = RequestState.UNKNOWN_ERROR
               Log.d(TAG, "getActivities: ${e.message}")
           }
        }
    }
}