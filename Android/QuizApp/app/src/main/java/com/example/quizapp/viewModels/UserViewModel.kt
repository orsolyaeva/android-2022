package com.example.quizapp.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quizapp.models.User

class UserViewModel : ViewModel() {
    private var user = User("")

    // set name of the user
    fun setName(name: String) {
        Log.d("SETNAME", "name: $name")
        user.changeName(name)
    }

    // set high score of the user
    fun setHighScore(highScore: Double) {
        if(highScore > user.getHighScore()) {
            user.changeHighScore(highScore)
        }
    }

    fun resetHighScore() {
        user.changeHighScore(0.0)
    }

    fun setProfilePicture(uri: Uri) {
        user.picture = uri
    }

    fun getProfilePicture(): Uri? {
        return user.picture
    }
}