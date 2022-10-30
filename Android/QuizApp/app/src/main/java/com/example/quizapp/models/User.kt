package com.example.quizapp.models

class User(var name: String?, var high_score: Int = 0) {
    // change the name of the user
    fun changeName(newName: String) {
        name = newName
    }

    // change the high score of the user
    fun changeHighScore(newHighScore: Int) {
        if (newHighScore > high_score) {
            high_score = newHighScore
        }
    }

    // get the name of the user
    @JvmName("getName1")
    fun getName(): String? {
        return name
    }

    // get the high score of the user
    fun getHighScore(): Int {
        return high_score
    }
}