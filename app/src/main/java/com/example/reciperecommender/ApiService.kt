package com.example.reciperecommender

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// API 요청 정의하는 파일 (RetrofitInstance 와 연계)
interface ApiService {
    @GET("recipes/findByIngredients")
    suspend fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String
    ): List<Recipe>

    // ✅ 레시피 상세 정보 API 추가
    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): RecipeDetail // ✅ RecipeDetail 데이터 모델 반환
}
