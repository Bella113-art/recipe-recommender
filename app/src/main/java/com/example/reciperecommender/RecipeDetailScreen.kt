package com.example.reciperecommender

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RecipeDetailScreen(navController: NavController, recipeId: Int?) {
    val recipe = remember { mutableStateOf<RecipeDetail?>(null) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(recipeId) {
        if (recipeId != null && recipeId != -1) { // ✅ `null` 체크 추가
            fetchRecipeDetail(recipeId, recipe, isLoading)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            recipe.value?.let { recipe ->
                Image(
                    painter = rememberImagePainter(recipe.image),
                    contentDescription = "Recipe Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(recipe.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ready in ${recipe.readyInMinutes} minutes", fontSize = 18.sp)
            } ?: Text("Recipe not found", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ✅ API 호출 함수 (레시피 상세 정보 가져오기)
fun fetchRecipeDetail(recipeId: Int, recipe: MutableState<RecipeDetail?>, isLoading: MutableState<Boolean>) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            isLoading.value = true
            val apiKey = BuildConfig.SPOONACULAR_API_KEY

            Log.d("API_CALL", "Fetching recipe details for ID: $recipeId")
            val response = RetrofitInstance.api.getRecipeDetails(recipeId, apiKey)
            Log.d("API_RESPONSE", "Recipe Details: ${response.toString()}")

            CoroutineScope(Dispatchers.Main).launch {
                recipe.value = response
                isLoading.value = false
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error fetching recipe details: ${e.message}")
            isLoading.value = false
        }
    }
}
