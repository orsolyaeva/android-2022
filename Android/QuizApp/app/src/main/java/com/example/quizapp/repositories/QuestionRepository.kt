package com.example.quizapp.repositories


import com.example.quizapp.models.BaseData
import com.example.quizapp.services.RetrofitService
import retrofit2.Response

object QuestionRepository {
    suspend fun getQuestions(amount: Int):Response<BaseData> {
        // get question synchronously using retrofit execute method
        val result = RetrofitService.api.getQuestions(amount)
        return result
    }
}