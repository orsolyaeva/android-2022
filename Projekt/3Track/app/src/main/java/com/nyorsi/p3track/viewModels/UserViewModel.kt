package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.models.UserModel
import com.nyorsi.p3track.repositories.UserRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class UserViewModel (application: Application) : AndroidViewModel(application) {
    val requestState: MutableLiveData<RequestState> = MutableLiveData()
    private val userRepository = UserRepository()
    var userList: MutableLiveData<List<UserModel>> = MutableLiveData()

    init {
        requestState.value = RequestState.LOADING
    }

    fun getMyUser(token: String?) {
        requestState.value = RequestState.LOADING
        if(token == null) {
            requestState.value = RequestState.INVALID_CREDENTIALS
            return
        }
        viewModelScope.launch {
            try {
                val response = userRepository.getMyUser(token)
                if(response.isSuccessful) {
                    requestState.value = RequestState.SUCCESS
                } else {
                    requestState.value = RequestState.INVALID_CREDENTIALS
                    Log.d(LoginViewModel.TAG, "login: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                requestState.value = RequestState.UNKNOWN_ERROR
                Log.d(LoginViewModel.TAG, "login: ${e.message}")
            }
        }
    }

    fun getUsers() {
        if(userList.value != null) {
            return
        }
        requestState.value = RequestState.LOADING
        viewModelScope.launch {
            try {
                val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                val token = sharedPref.getString("token", null)
                if(token != null) {
                    userList.value = listOf()
                    val response = userRepository.getUsers(token)
                    if (response.isSuccessful) {
                        val users = response.body()!!
                        for (user in users) {
                            userList.value = userList.value?.plus(
                                UserModel(
                                    user.ID,
                                    user.last_name,
                                    user.first_name,
                                    user.email,
                                    user.type,
                                    user.location,
                                    user.phone_number,
                                    user.department_id,
                                    user.image
                                )
                            )
                        }
                        Log.d("UserList", "getUsers: " + userList.value.toString())
                        requestState.value = RequestState.SUCCESS
                        Log.d("GlobalViewModel", "user: SUCCESS")
                    } else {
                        requestState.value = RequestState.INVALID_CREDENTIALS
                        Log.d(LoginViewModel.TAG, "login: ${response.errorBody()}")
                    }
                }
            } catch (e: Exception) {
                requestState.value = RequestState.UNKNOWN_ERROR
                Log.d("UserViewModel", "getUsers: ${e.message}")
            }
        }
    }

    fun getUserById(id: Int): UserModel? {
        for(user in userList.value!!) {
            if(user.id == id) {
                return user
            }
        }
        return null
    }
}