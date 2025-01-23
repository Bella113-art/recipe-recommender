package com.example.reciperecommender

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("search_history_prefs", Context.MODE_PRIVATE)

    // ✅ 검색어 저장
    fun saveSearchHistory(history: List<String>) {
        sharedPreferences.edit().putStringSet("search_history", history.toSet()).apply()
    }

    // ✅ 검색어 불러오기
    fun getSearchHistory(): List<String> {
        return sharedPreferences.getStringSet("search_history", emptySet())?.toList() ?: emptyList()
    }

    // ✅ 검색어 개별 삭제
    fun removeSearchItem(item: String) {
        val currentHistory = getSearchHistory().toMutableList()
        currentHistory.remove(item)
        saveSearchHistory(currentHistory)
    }

    // ✅ 검색어 전체 삭제
    fun clearSearchHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }
}
