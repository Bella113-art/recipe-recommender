package com.example.reciperecommender

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeResultsScreen(navController: NavController, query: String) {
    val decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8.toString()) // ✅ 디코딩 적용
    val recipes = remember { mutableStateListOf<Recipe>() }
    val isLoading = remember { mutableStateOf(false) } // ✅ 로딩 상태 추가

    LaunchedEffect(decodedQuery) {
        fetchRecipes(decodedQuery, recipes, isLoading)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Results") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp)) // 🔄 로딩 표시
            } else if (recipes.isEmpty()) {
                Text(
                    text = "No recipes found for \"$decodedQuery\"",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(recipes) { recipe ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { navController.navigate("recipeDetailScreen/${recipe.id.toString()}") }, // ✅ 상세 페이지로 이동
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(recipe.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Ready in ${recipe.readyInMinutes ?: "Unknown"} minutes")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ✅ API 호출 함수 추가
fun fetchRecipes(ingredients: String, recipes: MutableList<Recipe>, isLoading: MutableState<Boolean>) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            isLoading.value = true // 로딩 시작
            val apiKey = BuildConfig.SPOONACULAR_API_KEY // ✅ BuildConfig에서 API 키 불러오기

            Log.d("API_CALL", "요청 보냄: ingredients=$ingredients, apiKey=$apiKey")
            val response = RetrofitInstance.api.getRecipes(ingredients, apiKey)
            Log.d("API_RESPONSE", "응답 받음: ${response.toString()}")

            if (response.isEmpty()) {
                Log.e("API_ERROR", "No recipes found for \"$ingredients\"")
            }

            // UI 업데이트를 위해 Main Thread에서 실행
            CoroutineScope(Dispatchers.Main).launch {
                recipes.clear()
                recipes.addAll(response)
                isLoading.value = false // 로딩 완료
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "API 요청 중 오류 발생: ${e.message}")
            isLoading.value = false
        }
    }
}
