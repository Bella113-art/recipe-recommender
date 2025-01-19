package com.example.reciperecommender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("홈 화면") })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate("search") },
                    label = { Text("검색할 재료 입력") },
                    singleLine = true,
                    enabled = false // 사용자가 직접 입력하지 않고, 클릭하면 검색 화면으로 이동하도록 설정
                )
                IconButton(onClick = { navController.navigate("search") }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "검색")
                }
            }
        }
    }
}
