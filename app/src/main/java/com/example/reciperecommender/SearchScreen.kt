package com.example.reciperecommender

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val ingredientInput = remember { mutableStateOf("") }
    val context = LocalContext.current
    val searchHistoryManager = remember { SearchHistoryManager(context) }
    var searchHistory by remember { mutableStateOf(searchHistoryManager.getSearchHistory()) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isDropdownVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("재료 검색") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                TextField(
                    value = ingredientInput.value,
                    onValueChange = {
                        ingredientInput.value = it
                        isDropdownVisible = it.isNotEmpty()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isDropdownVisible = focusState.hasFocus
                        },
                    label = { Text("재료를 입력하세요") },
                    singleLine = true
                )
                IconButton(onClick = {
                    if (ingredientInput.value.isNotEmpty()) {
                        val query = ingredientInput.value.trim()
                        if (query.isNotEmpty()) {
                            if (!searchHistory.contains(query)) {
                                searchHistory = (searchHistory + query).takeLast(10)
                                searchHistoryManager.saveSearchHistory(searchHistory)
                            }
                            ingredientInput.value = ""
                            isDropdownVisible = false

                            navController.navigate("recipeResultsScreen/$query")
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "검색")
                }
            }

            // 🔥 검색어 삭제 버튼 추가
            if (searchHistory.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        searchHistoryManager.clearSearchHistory()
                        searchHistory = emptyList()
                    }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "모든 검색어 삭제")
                    }
                }
            }

            // 🔥 최근 검색어 표시 (Dropdown 형태로 표시)
            if (isDropdownVisible && searchHistory.isNotEmpty()) {
                SearchHistoryDropdown(searchHistory,
                    onItemClick = { selectedItem ->
                        ingredientInput.value = selectedItem
                        isDropdownVisible = false
                    },
                    onDeleteItem = { item ->
                        searchHistoryManager.removeSearchItem(item)
                        searchHistory = searchHistoryManager.getSearchHistory()
                    }
                )
            }
        }
    }
}

// ✅ 최근 검색어 Dropdown (삭제 버튼 포함)
@Composable
fun SearchHistoryDropdown(
    searchHistory: List<String>,
    onItemClick: (String) -> Unit,
    onDeleteItem: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(searchHistory) { historyItem ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(historyItem) }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = historyItem, fontSize = 14.sp)
                IconButton(onClick = { onDeleteItem(historyItem) }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "삭제")
                }
            }
            Divider()
        }
    }
}
