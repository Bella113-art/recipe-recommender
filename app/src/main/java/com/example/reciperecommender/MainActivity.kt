package com.example.reciperecommender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reciperecommender.ui.theme.RecipeRecommenderTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RecipeRecommenderTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                // 현재 화면(Route)에 따라 제목과 뒤로가기 버튼 설정
                val currentScreen = backStackEntry?.destination?.route ?: "home"
                val showBackButton = currentScreen != "home"
                val screenTitle = when {
                    currentScreen.startsWith("search") -> "재료 검색"
                    currentScreen.startsWith("recipeResultsScreen") -> "검색 결과"
                    currentScreen.startsWith("recipeDetailScreen") -> "레시피 상세 정보"
                    else -> "홈"
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(screenTitle) },
                            navigationIcon = {
                                if (showBackButton) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                                    }
                                }
                            } // ✅ `else null` 제거 (TopAppBar에서는 `null`을 허용하지 않음!)
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(navController) }
                        composable("search") { SearchScreen(navController) }
                        composable("recipeResultsScreen/{query}") { backStackEntry ->
                            val query = backStackEntry.arguments?.getString("query") ?: ""
                            RecipeResultsScreen(navController, query)
                        }
                        composable("recipeDetailScreen/{id}") { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: -1
                            RecipeDetailScreen(navController, recipeId)
                        }
                    }
                }
            }
        }
    }
}
