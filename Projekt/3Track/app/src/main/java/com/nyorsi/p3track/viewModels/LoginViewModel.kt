package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.api.queryModels.login.LoginRequest
import com.nyorsi.p3track.repositories.UserRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }

    val requestState: MutableLiveData<RequestState> = MutableLiveData()
    private val userRepository = UserRepository()

    fun login(email: String, password: String) {
        requestState.value = RequestState.LOADING
        if(email.isEmpty() || password.isEmpty()) {
            requestState.value = RequestState.INVALID_CREDENTIALS
            return
        }
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = userRepository.loginUser(loginRequest)
                if(response.isSuccessful) {
                    requestState.value = RequestState.SUCCESS
                    val deadline = response.body()!!.deadline
                    val token = response.body()!!.token
                    Log.d(TAG, "login: $deadline")
                    Log.d(TAG, "login: $token")
                    val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("deadline", deadline.toString())
                        apply()
                    }
                    with(sharedPref.edit()) {
                        putString("token", token)
                        apply()
                    }
                } else {
                    requestState.value = RequestState.INVALID_CREDENTIALS
                    Log.d(TAG, "login: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                requestState.value = RequestState.UNKNOWN_ERROR
                Log.d(TAG, "login: ${e.message}")
            }
        }
    }
}