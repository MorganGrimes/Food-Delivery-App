package com.example.fooddeliveryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fooddeliveryapp.data.remote.RetrofitInstance

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    suspend fun fetchCategories() {
        try {
            val response = RetrofitInstance.api.getCategories()
            _categories.value = response.categories
        } catch (e: Exception) {
            _categories.value = listOf("Error", "Try Again")
        }
    }
}