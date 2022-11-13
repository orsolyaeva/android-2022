package com.example.quizapp.models

import com.google.gson.annotations.SerializedName

data class BaseData(
    @SerializedName("response_code")
    val responseCode : Int,
    val results : List<Results>
)

data class Results(
    val category : String,
    val type : String,
    val difficulty : String,
    val question : String,

    @SerializedName("correct_answer")
    val correctAnswer : String,

    @SerializedName("incorrect_answers")
    val incorrectAnswers : List<String>
)
