package com.example.quizapp.models

import android.net.Uri

class User(var name: String?, var high_score: Double = 0.0, var picture: Uri? = null) {
    // change the name of the user
    fun changeName(newName: String) {
        name = newName
    }

    // change the high score of the user
    fun changeHighScore(newHighScore: Double) {
        high_score = newHighScore
    }

    // get the name of the user
    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    // get the high score of the user
    fun getHighScore(): Double {
        return high_score
    }
}