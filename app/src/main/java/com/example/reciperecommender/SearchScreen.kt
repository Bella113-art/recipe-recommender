package com.example.reciperecommender

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val ingredientInput = remember { mutableStateOf("") }
    val searchHistory = remember { mutableStateListOf<String>() }
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
                        isDropdownVisible = it.isNotEmpty() // 입력하면 검색 기록 보이도록 설정
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
                                if (searchHistory.size >= 5) {
                                    searchHistory.removeAt(0) // 최대 5개까지 저장
                                }
                                searchHistory.add(query)
                            }
                            navController.navigate("results/$query")
                            ingredientInput.value = "" // 검색 후 입력 필드 비우기
                            isDropdownVisible = false
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "검색")
                }
            }

            // 🔥 최근 검색어 표시 (Dropdown 형태로 표시)
            if (isDropdownVisible && searchHistory.isNotEmpty()) {
                SearchHistoryDropdown(searchHistory) { selectedItem ->
                    ingredientInput.value = selectedItem
                    isDropdownVisible = false
                }
            }
        }
    }
}

// ✅ 최근 검색어 Dropdown
@Composable
fun SearchHistoryDropdown(searchHistory: List<String>, onItemClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(searchHistory) { historyItem ->
            Text(
                text = historyItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(historyItem) }
                    .padding(8.dp),
                fontSize = 14.sp
            )
            Divider() // 구분선 추가
        }
    }
}
