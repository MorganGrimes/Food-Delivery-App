package com.example.fooddeliveryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
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
    private var fullRestaurantList: List<Restaurants> = emptyList()

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _restaurants = MutableLiveData<List<Restaurants>>()
    val restaurants: LiveData<List<Restaurants>> = _restaurants

    private val _filteredRestaurants = MutableLiveData<List<Restaurants>>()
    val filteredRestaurantsLiveData: LiveData<List<Restaurants>> = _filteredRestaurants

    private val _popularFoodItems = MutableLiveData<List<PopularFoodItemModel>>()
    val popularFoodItemsLiveData: LiveData<List<PopularFoodItemModel>> = _popularFoodItems

    private val _cartItems = MutableLiveData<MutableList<CartItemModel>>(mutableListOf())
    val cartItems: MutableLiveData<MutableList<CartItemModel>> get() = _cartItems

    suspend fun fetchCategories() {
        try {
            val response = RetrofitInstance.categoryApi.getCategories()
            _categories.value = response.categories
        } catch (e: Exception) {
            _categories.value = listOf(ERROR, TRY_AGAIN)
        }
    }

    private fun setFullRestaurantList(restaurants: List<Restaurants>) {
        fullRestaurantList = restaurants
        _restaurants.postValue(restaurants)
        _filteredRestaurants.postValue(restaurants)
        updatePopularFood()
    }

    fun getAllRestaurants(): List<Restaurants> = fullRestaurantList

    fun filterRestaurantsByCategory(category: String) {
        val filtered = fullRestaurantList.filter { it.food.containsKey(category) }
        _filteredRestaurants.value = filtered
        updatePopularFood()
    }

    fun filterRestaurants(
        deliveryTimeRange: IntRange? = null,
        pricingRange: ClosedFloatingPointRange<Double>? = null,
        minRating: Int? = null
    ) {
        val filtered = fullRestaurantList.filter { restaurant ->
            val timeString = restaurant.deliveryTime.replace("[^0-9\\-]".toRegex(), "")
            val times = timeString.split("-").mapNotNull { it.toIntOrNull() }
            val minTime = times.minOrNull() ?: 0
            val maxTime = times.maxOrNull() ?: 0
            val deliveryOk = deliveryTimeRange?.let { minTime >= it.first && maxTime <= it.last } ?: true

            val prices = restaurant.food.values.flatten().map { it.price }
            val avgPrice = if (prices.isNotEmpty()) prices.average() else 0.0
            val pricingOk = pricingRange?.let { avgPrice in it } ?: true

            val ratingFloor = restaurant.rating.toInt()
            val ratingOk = minRating?.let { ratingFloor >= it } ?: true

            deliveryOk && pricingOk && ratingOk
        }

        _filteredRestaurants.value = filtered
        updatePopularFood()
    }

    fun fetchRestaurants() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.restaurantApi.getRestaurants()
                if (response.isSuccessful) {
                    val list = response.body()?.restaurants ?: emptyList()
                    setFullRestaurantList(list)
                } else {
                    _restaurants.postValue(emptyList())
                    _filteredRestaurants.postValue(emptyList())
                }
            } catch (e: Exception) {
                _restaurants.postValue(emptyList())
                _filteredRestaurants.postValue(emptyList())
            }
        }
    }

    private fun updatePopularFood() {
        val category = selectedFoodCategory.value ?: return
        val restaurants = _filteredRestaurants.value ?: return

        val foodWithRestaurant = restaurants.flatMap { restaurant ->
            restaurant.food[category].orEmpty().map { foodItem ->
                Pair(foodItem, restaurant)
            }
        }.sortedByDescending { (foodItem, _) -> foodItem.eatenLastMonth }

        val foodModels = foodWithRestaurant.map { (foodItem, restaurant) ->
            PopularFoodItemModel(
                popularFoodImage = com.example.fooddeliveryapp.R.drawable.ic_launcher_background,
                popularFoodName = foodItem.name,
                popularFoodRestaurantName = restaurant.name,
                popularFoodPrice = "$${foodItem.price}",
                popularFoodRestaurantId = restaurant.id
            )
        }

        _popularFoodItems.value = foodModels
    }

    fun refreshRestaurants() {
        _restaurants.value = fullRestaurantList
        _filteredRestaurants.value = fullRestaurantList
    }

    fun addToCart(
        imageRes: Int,
        foodName: String,
        price: Double,
        size: String,
        quantity: Int,
        restaurantId: Int
    ) {
        val item = CartItemModel(
            cartImage = imageRes,
            cartFoodName = foodName,
            cartFoodPrice = price * quantity,
            cartFoodSize = size,
            cartFoodQuantity = quantity,
            restaurantId = restaurantId
        )
        _cartItems.value?.add(item)
        _cartItems.value = _cartItems.value
    }
}

