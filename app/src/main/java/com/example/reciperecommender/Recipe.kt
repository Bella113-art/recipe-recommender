package com.example.reciperecommender

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: Int // ✅ `readyInMinutes` 속성 추가
)
