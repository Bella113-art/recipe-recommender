package com.example.reciperecommender

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SearchScreen(navController: NavController) {
    val ingredientInput = remember { mutableStateOf("") }
    val context = LocalContext.current
    val searchHistoryManager = remember { SearchHistoryManager(context) }
    var searchHistory by remember { mutableStateOf(searchHistoryManager.getSearchHistory()) }
    var isDropdownVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top, // 🔥 상단 정렬 유지
        horizontalAlignment = Alignment.CenterHorizontally // 🔥 가로 중앙 정렬
    ) {
        // 🔍 검색 입력창
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = ingredientInput.value,
                onValueChange = {
                    ingredientInput.value = it
                    isDropdownVisible = it.isNotEmpty()
                },
                modifier = Modifier.weight(1f),
                label = { Text("재료를 입력하세요") },
                singleLine = true
            )
            IconButton(onClick = {
                search(navController, ingredientInput.value, searchHistory, searchHistoryManager)
            }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "검색")
            }
        }

        // 🗑️ 전체 검색 기록 삭제 버튼 (정렬 수정)
        if (searchHistory.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    searchHistoryManager.clearSearchHistory()
                    searchHistory = emptyList()
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "모든 검색어 삭제")
                }
                Text("전체 삭제", fontSize = 14.sp)
            }
        }

        // 🔥 최근 검색어 목록 (아이템 클릭 시 자동 검색)
        if (searchHistory.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(searchHistory) { historyItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                ingredientInput.value = historyItem // ✅ 검색창에 입력
                                search(navController, historyItem, searchHistory, searchHistoryManager) // ✅ 자동 검색 실행
                            }
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = historyItem)
                        IconButton(onClick = {
                            searchHistoryManager.removeSearchItem(historyItem)
                            searchHistory = searchHistoryManager.getSearchHistory()
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "삭제")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

// ✅ 검색 실행 함수 (중복 로직 방지)
fun search(
    navController: NavController,
    query: String,
    searchHistory: List<String>,
    searchHistoryManager: SearchHistoryManager
) {
    if (query.isNotEmpty()) {
        if (!searchHistory.contains(query)) {
            val updatedHistory = (searchHistory + query).takeLast(10)
            searchHistoryManager.saveSearchHistory(updatedHistory)
        }
        navController.navigate("recipeResultsScreen/$query")
    }
}
