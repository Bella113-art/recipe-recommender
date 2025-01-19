package com.example.reciperecommender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

                NavHost(navController, startDestination = "home") {  // 홈 화면이 첫 화면으로 설정됨
                    composable("home") { HomeScreen(navController) }  // 홈 화면
                    composable("search") { SearchScreen(navController) }  // 검색 화면
                    composable("results/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        RecipeResultsScreen(navController, query)
                    }
                }

            }
        }
    }
}
