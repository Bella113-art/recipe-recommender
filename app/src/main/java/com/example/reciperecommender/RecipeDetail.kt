package com.example.reciperecommender

// 개별 레시피 상세 정보 표시 -> RecipeDetailScreen.kt
data class RecipeDetail(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int,
    val servings: Int, // ✅ 몇 인분인지
    val instructions: String // ✅ 조리 방법
)
