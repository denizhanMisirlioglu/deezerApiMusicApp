package com.example.musicapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.Data.Category
import com.example.musicapp.Data.Repository.DeezerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val deezerRepository = DeezerRepository()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>>
        get() = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val categories = deezerRepository.getCategories()
                _categories.value = categories
            } catch (e: Exception) {
                Log.e("CategoriesViewModel", "Failed to fetch categories: ${e.message}")
            }
        }
    }
}




