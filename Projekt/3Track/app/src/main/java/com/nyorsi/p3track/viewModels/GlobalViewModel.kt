package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.MainActivity
import com.nyorsi.p3track.models.*
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch


class GlobalViewModel(application: Application) : AndroidViewModel(application) {
    private val activityViewModel: ActivityViewModel by lazy { ActivityViewModel(application) }
    private val userViewModel: UserViewModel by lazy { UserViewModel(application) }
    private val departmentViewModel: DepartmentViewModel by lazy { DepartmentViewModel(application) }
    private val taskViewModel: TaskViewModel by lazy { TaskViewModel(application) }
    private var currentUser: UserModel? = null

    val requestState: MutableLiveData<RequestState> = MutableLiveData()

    private var _activityList: MutableLiveData<List<ActivityModel>> = MutableLiveData()
    private var _taskList: MutableLiveData<List<TaskModel>> = MutableLiveData()

    companion object {
        const val TAG = "GlobalViewModel"
    }

    fun loadActivities() {
        val liveDataMerger = MediatorLiveData<Int>()
        requestState.value = RequestState.LOADING
        _activityList.value = listOf()
        _taskList.value = listOf()

        userViewModel.getUsers()
        activityViewModel.getActivities()
        departmentViewModel.getDepartments()
        taskViewModel.getTasks()

        liveDataMerger.value = 0

        liveDataMerger.addSource(userViewModel.requestState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadUsers: ${userViewModel.userList.value}")
            }
        }

        liveDataMerger.addSource(activityViewModel.getActivitiesState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadActivities: ${liveDataMerger.value}")
            }
        }

        liveDataMerger.addSource(departmentViewModel.getDepartmentsState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadDepartments: ${liveDataMerger.value}")
            }
        }

        liveDataMerger.addSource(taskViewModel.getTasksState) {
            if (it == RequestState.SUCCESS) {
                liveDataMerger.value = liveDataMerger.value?.plus(1)
                Log.d(TAG, "loadTasks: ${liveDataMerger.value}")
            }
        }

        liveDataMerger.observeForever { it ->
            if (it == 4) {
                Log.d(TAG, "GYERE MAR")
                val userList = userViewModel.userList.value
                val activityList = activityViewModel.activityList.value
                val taskList = taskViewModel.taskList.value
                val departmentList = departmentViewModel.departmentList.value

                Log.d(TAG, "userList: ${userList?.size}")
                Log.d(TAG, "activityList: ${activityList?.size}")
                Log.d(TAG, "taskList: ${taskList?.size}")
                Log.d(TAG, "departmentList: ${departmentList?.size}")


                for(task in taskList!!) {
//                    Log.d(TAG, "loadActivities: ${task}")
                    val createdByUser = userList?.find { user -> user.id == task.created_by_user_ID }
                    val assignedToUser = userList?.find { user -> user.id == task.assigned_to_user_ID }
                    val department = departmentList?.find { department -> department.id == task.department_ID }

                    _taskList.value = _taskList.value?.plus(
                        TaskModel( task.ID,
                            task.title, task.description, task.created_time, createdByUser, assignedToUser,
                            if(task.priority == 0) TaskPriority.LOW else if(task.priority < 4) TaskPriority.MEDIUM else TaskPriority.HIGH,
                            task.deadline, department,
                            if(task.status < 0) TaskStatus.BLOCKED else if(task.status < 3) TaskStatus.values()[task.status] else TaskStatus.NEW,
                            task.progress)
                    )
//                    Log.d(TAG, "taskList: ${_taskList.value}")
                }

                Log.d(TAG, "taskList.size: ${_taskList.value?.size}")

                for(activity in activityList!!) {
                    val user = userList?.find { it.id == activity.created_by_user_id }
                    val subUser = userList?.find { it.id == activity.sub_user_ID }
                    _activityList.value  = _activityList.value?.plus(ActivityModel(activity.ID,
                        ActivityType.values()[activity.type],
                        ActivitySubType.values()[activity.sub_type + 1],
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

    fun getDepartmentList() = departmentViewModel.departmentList

    fun getTaskList() = _taskList

    fun getLoggedInUser() {
        requestState.value = RequestState.LOADING
        val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", 0)
        val token = sharedPref.getString("token", null)

       userViewModel.getMyUser(token!!)

        userViewModel.requestState.observeForever {
            if (it == RequestState.SUCCESS) {
                val userTemp = userViewModel.getCurrentUser()!!
                currentUser = UserModel(userTemp.ID, userTemp.last_name, userTemp.first_name,
                   userTemp.email, UserType.values()[userTemp.type], userTemp.location,
                   userTemp.phone_number, userTemp.department_id, userTemp.image)
                requestState.value = RequestState.SUCCESS
            }
        }
    }

    fun getCurrentUser() = currentUser
}
