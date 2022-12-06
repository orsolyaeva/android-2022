package com.nyorsi.p3track.ui.login

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.api.login.LoginRequest
import com.nyorsi.p3track.api.login.LoginResponse
import com.nyorsi.p3track.repositories.UserRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }

    val loginResult: MutableLiveData<LoginResult> = MutableLiveData()
    private val userRepository = UserRepository()

    fun login(email: String, password: String) {
        loginResult.value = LoginResult.LOADING
        if(email.isEmpty() || password.isEmpty()) {
            loginResult.value = LoginResult.INVALID_CREDENTIALS
            return
        }
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = userRepository.loginUser(loginRequest)
                if(response.isSuccessful) {
                    loginResult.value = LoginResult.SUCCESS
                    val deadline = response.body()!!.deadline
                    val sharedPref = getApplication<Application>().getSharedPreferences("P3Track", AppCompatActivity.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("deadline", deadline.toString())
                        apply()
                    }
                } else {
                    loginResult.value = LoginResult.INVALID_CREDENTIALS
                    Log.d(TAG, "login: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                loginResult.value = LoginResult.UNKNOWN_ERROR
                Log.d(TAG, "login: ${e.message}")
            }
        }
    }
}