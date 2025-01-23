package com.example.reciperecommender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

                Scaffold(
                    topBar = { TopAppBar(title = { Text("Recipe Recommender") }) }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NavHost(navController, startDestination = "search") { // ✅ 첫 화면을 "search"로 변경
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
}
