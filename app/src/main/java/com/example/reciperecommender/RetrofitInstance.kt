package com.example.reciperecommender

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 	API 연동 설정하는 파일
object RetrofitInstance {
    private const val BASE_URL = "https://api.spoonacular.com/" // Spoonacular API 기본 URL

    val api: ApiService by lazy {
        Log.d("RetrofitInstance", "Retrofit API 객체 생성됨") // ✅ 로그 추가
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 Kotlin 객체로 변환
            .build()
            .create(ApiService::class.java)
    }
}
