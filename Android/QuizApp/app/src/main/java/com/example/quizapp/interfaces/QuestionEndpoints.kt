package com.example.quizapp.interfaces

import com.example.quizapp.models.BaseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionEndpoints {
    @GET("api.php")
    suspend fun getQuestions(@Query("amount") amount :Int ): Response<BaseData>
}