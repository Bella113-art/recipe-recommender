package com.example.reciperecommender

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("recipes/findByIngredients")
    suspend fun getRecipes(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String
    ): List<Recipe>
}
