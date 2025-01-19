package com.example.reciperecommender

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.spoonacular.com/" // Spoonacular API 기본 URL

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 Kotlin 객체로 변환
            .build()
            .create(ApiService::class.java)
    }
}
