package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.nyorsi.p3track.models.ActivityModel
import com.nyorsi.p3track.models.ActivitySubType
import com.nyorsi.p3track.models.ActivityType
import com.nyorsi.p3track.utils.RequestState


class GlobalViewModel(application: Application) : AndroidViewModel(application) {
    private val activityViewModel: ActivityViewModel by lazy { ActivityViewModel(application) }
    private val userViewModel: UserViewModel by lazy { UserViewModel(application) }

    val requestState: MutableLiveData<RequestState> = MutableLiveData()

    private var _activityList: MutableLiveData<List<ActivityModel>> = MutableLiveData()

    companion object {
        const val TAG = "GlobalViewModel"
    }

    fun loadActivities() {
        val liveDataMerger = MediatorLiveData<Int>()
        _activityList.value = listOf()
        userViewModel.getUsers()
        activityViewModel.getActivities()
        liveDataMerger.value = 0

        liveDataMerger.addSource(userViewModel.requestState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadActivities: ${userViewModel.userList.value}")
            }
        }

        liveDataMerger.addSource(activityViewModel.getActivitiesState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadActivities: ${liveDataMerger.value}")
            }
        }

        liveDataMerger.observeForever { it ->
            if (it == 2) {
                val userList = userViewModel.userList.value
                val activityList = activityViewModel.activityList.value
                
                for(activity in activityList!!) {
                    val user = userList?.find { it.id == activity.created_by_user_id }
                    val subUser = userList?.find { it.id == activity.sub_user_ID }
                    _activityList.value  = _activityList.value?.plus(ActivityModel(activity.ID,
                        ActivityType.values()[activity.type],
                        ActivitySubType.values()[activity.sub_type],
                        user, activity.created_time, activity.sub_ID,
                        subUser, activity.note, activity.progress,))
                }
                requestState.value = RequestState.SUCCESS
            }
        }
    }

    fun getActivityList(): MutableLiveData<List<ActivityModel>> {
        return _activityList
    }

    fun getUserList() = userViewModel.userList
}
