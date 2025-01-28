package com.example.reciperecommender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { /* 🔥 뒤로 가기 버튼 없음! */ }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔥 로고 대신 텍스트 표시
            Text(
                text = "Recipe Recommender",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 🔍 검색창 (클릭하면 SearchScreen.kt로 이동)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clickable { navController.navigate("search") }, // 🔥 검색창 클릭 시 `SearchScreen.kt`로 이동
                label = { Text("검색할 재료 입력") },
                singleLine = true,
                enabled = false, // 입력 불가능, 클릭만 가능
                trailingIcon = {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "검색")
                }
            )
        }
    }
}
