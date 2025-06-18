package com.example.fooddeliveryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fooddeliveryapp.data.remote.RetrofitInstance
import com.example.fooddeliveryapp.data.remote.dto.restaurant.Restaurants
import com.example.fooddeliveryapp.utils.ERROR
import com.example.fooddeliveryapp.utils.TRY_AGAIN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var selectedRestaurantId: Int? = null
    val selectedCategory = MutableLiveData<String>()
    val selectedFoodCategory = MutableLiveData<String>()

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _restaurants = MutableLiveData<List<Restaurants>>()
    val restaurants: LiveData<List<Restaurants>> = _restaurants

    suspend fun fetchCategories() {
        try {
            val response = RetrofitInstance.categoryApi.getCategories()
            _categories.value = response.categories
        } catch (e: Exception) {
            _categories.value = listOf(ERROR, TRY_AGAIN)
        }
    }

    fun fetchRestaurants() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.restaurantApi.getRestaurants()
                if (response.isSuccessful) {
                    _restaurants.postValue(response.body()?.restaurants ?: emptyList())
                } else {
                    _restaurants.postValue(emptyList())
                }
            } catch (e: Exception) {
                _restaurants.postValue(emptyList())
            }
        }
    }

    fun getAllRestaurants(): List<Restaurants> {
        return _restaurants.value.orEmpty()
    }
}