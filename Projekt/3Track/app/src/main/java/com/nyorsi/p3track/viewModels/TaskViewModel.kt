package com.nyorsi.p3track.viewModels

import android.app.Application
import android.app.DownloadManager.Request
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.api.queryModels.task.CreateTaskRequest
import com.nyorsi.p3track.api.queryModels.task.GetTasksResponse
import com.nyorsi.p3track.api.queryModels.task.UpdateTaskRequest
import com.nyorsi.p3track.models.TaskModel
import com.nyorsi.p3track.repositories.TaskRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class TaskViewModel(application: Application): AndroidViewModel(application) {
    private val taskRepository = TaskRepository()
    val getTasksState: MutableLiveData<RequestState> = MutableLiveData()
    val createTaskState: MutableLiveData<RequestState> = MutableLiveData()
    val updateTaskState: MutableLiveData<RequestState> = MutableLiveData()
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

    fun createTask(token: String, createTaskRequest: CreateTaskRequest) {
        createTaskState.value = RequestState.LOADING
        Log.d("TaskViewModel", "createTaskRequest: ${createTaskRequest}")
        viewModelScope.launch {
            try {
                Log.d("TaskViewModel", "token: $token")
                val response = taskRepository.createTask(token, createTaskRequest)
                Log.d("TaskViewModel", "createTaskResponse: $response")
                if(response.isSuccessful) {
                    createTaskState.value = RequestState.SUCCESS
                } else {
                    createTaskState.value = RequestState.UNKNOWN_ERROR
                }
            } catch (e: Exception) {
                createTaskState.value = RequestState.UNKNOWN_ERROR
            }
        }
    }

    fun updateTask(token: String, updateTaskRequest: UpdateTaskRequest) {
        updateTaskState.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val response = taskRepository.updateTask(token, updateTaskRequest)
                if(response.isSuccessful) {
                    updateTaskState.value = RequestState.SUCCESS
                } else {
                    updateTaskState.value = RequestState.UNKNOWN_ERROR
                }
            } catch (e: Exception) {
                updateTaskState.value = RequestState.UNKNOWN_ERROR
            }
        }
    }
}