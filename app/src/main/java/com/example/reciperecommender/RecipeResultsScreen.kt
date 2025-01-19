package com.example.reciperecommender

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeResultsScreen(navController: NavController, query: String) {
    val recipes = remember { mutableStateOf<List<Recipe>>(emptyList()) }

    // 임시: query 값 로그 출력
    println("RecipeResultsScreen - Query: $query")

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
            if (recipes.value.isEmpty()) {
                Text(
                    text = "No recipes found for \"$query\"",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(recipes.value) { recipe ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
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
