package com.nyorsi.p3track.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nyorsi.p3track.repositories.UserRepository
import com.nyorsi.p3track.utils.RequestState
import kotlinx.coroutines.launch

class UserViewModel (application: Application) : AndroidViewModel(application) {
    val requestState: MutableLiveData<RequestState> = MutableLiveData()
    private val userRepository = UserRepository()

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
}