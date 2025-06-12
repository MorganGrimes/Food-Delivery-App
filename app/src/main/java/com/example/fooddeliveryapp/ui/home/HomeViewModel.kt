package com.example.fooddeliveryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fooddeliveryapp.data.remote.RetrofitInstance
import com.example.fooddeliveryapp.data.remote.dto.RestaurantResponse
import com.example.fooddeliveryapp.utils.ERROR
import com.example.fooddeliveryapp.utils.TRY_AGAIN

class HomeViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _restaurants = MutableLiveData<List<RestaurantResponse>>()
    val restaurants: LiveData<List<RestaurantResponse>> = _restaurants

        suspend fun fetchCategories() {
        try {
            val response = RetrofitInstance.categoryApi.getCategories()
            _categories.value = response.categories
        } catch (e: Exception) {
            _categories.value = listOf(ERROR, TRY_AGAIN)
        }
    }

    suspend fun fetchRestaurants() {
        try {
            val response = RetrofitInstance.restaurantApi.getRestaurants()
            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                _restaurants.value = body
            } else {
                _restaurants.value = emptyList()
            }
        } catch (e: Exception) {
            _restaurants.value = emptyList()
        }
    }
}