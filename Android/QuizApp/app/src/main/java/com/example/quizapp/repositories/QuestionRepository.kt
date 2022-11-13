package com.example.quizapp.repositories


import com.example.quizapp.models.BaseData
import com.example.quizapp.services.RetrofitService
import retrofit2.Response

object QuestionRepository {
    suspend fun getQuestions(amount: Int):Response<BaseData> {
        val result = RetrofitService.api.getQuestions(amount)
        return result
    }
}