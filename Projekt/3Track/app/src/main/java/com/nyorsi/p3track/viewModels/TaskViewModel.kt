package com.nyorsi.p3track.viewModels

import android.app.Application
import android.app.DownloadManager.Request
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.api.queryModels.task.GetTasksResponse
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.repositories.TaskRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val taskRepository = TaskRepository()
    val getTasksState: MutableLiveData<RequestState> = MutableLiveData()
    val taskList: MutableLiveData<List<GetTasksResponse>> = MutableLiveData()

    fun getTasks() {
        getTasksState.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                val token = sharedPref.getString("token", null)
                if(token != null) {
                    taskList.value = listOf()
                    val response = taskRepository.getTasks(token)
                    if(response.isSuccessful) {
                        val taskListResponse = response.body()!!
                        for (task in taskListResponse) {
                            taskList.value = taskList.value?.plus(task)
                        }
                        Log.d("TaskViewModel", "getTasks: ${taskList.value}")
                        getTasksState.value = RequestState.SUCCESS
                        Log.d("GlobalViewModel", "task: SUCCESS")
                    } else {
                        Log.d("GlobalViewModel", "task: ERROR")
                        getTasksState.value = RequestState.UNKNOWN_ERROR
                    }
                }
            } catch (e: Exception) {
                Log.d("GlobalViewModel", "task: ERROR2")
                Log.d("GlobalViewModel", "task: ${e.message}")
                getTasksState.value = RequestState.UNKNOWN_ERROR
            }
        }
    }
}