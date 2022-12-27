package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.api.queryModels.activities.GetActivitiesResponse
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
    private val globalViewModel: GlobalViewModel by lazy { GlobalViewModel(application) }
    val getActivitiesState: MutableLiveData<RequestState> = MutableLiveData()
    var activityList : MutableLiveData<List<GetActivitiesResponse>> = MutableLiveData()

    fun getActivities() {
        getActivitiesState.value = RequestState.LOADING
        viewModelScope.launch {
           try {
               val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
               val token = sharedPref.getString("token", null)
               if(token != null) {
                   activityList.value = listOf()
                   val response = activityRepository.getActivities(token)
                   if(response.isSuccessful) {
                       val activityLog = response.body()!!
                       for (activity in activityLog) {
                           activityList.value = activityList.value?.plus(activity)
                       }
                       Log.d("ActivityList", "getActivities: " + activityList.value.toString())
                       getActivitiesState.value = RequestState.SUCCESS
                   } else {
                          getActivitiesState.value = RequestState.UNKNOWN_ERROR
                          Log.d(TAG, "getActivities: ${response.errorBody()}")
                   }
               }
           } catch (e: Exception) {
               getActivitiesState.value = RequestState.UNKNOWN_ERROR
               Log.d(TAG, "getActivitiesError: ${e.message}")
           }
        }
    }
}