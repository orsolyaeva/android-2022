package com.example.quizapp.viewModels

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
    fun setHighScore(highScore: Int) {
        if(highScore > user.getHighScore()) {
            user.changeHighScore(highScore)
        }
    }

    // get name of the user
    fun getName(): String? {
        return user.getName()
    }

    // get high score of the user
    fun getHighScore(): Int {
        return user.getHighScore()
    }
}