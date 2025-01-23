package com.example.reciperecommender

// 검색 결과 목록에 표시할 간단한 정보 -> RecipeResultsScreen.kt
data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int // ✅ `readyInMinutes` 속성 추가
)
