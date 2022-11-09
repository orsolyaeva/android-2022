package com.example.quizapp.models

import android.content.Context
import android.net.Uri

class User(var name: String?, var high_score: Double = 0.0, var picture: Uri? = null) {
    // change name of the user in shared preferences
    fun changeName(name: String) {
        this.name = name
    }

    // change the high score of the user in shared preferences
    fun changeHighScore(highScore: Double) {
        this.high_score = highScore
    }

    // get the name of the user from shared preferences
    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    // get the high score of the user
    fun getHighScore(): Double {
        return high_score
    }
}

