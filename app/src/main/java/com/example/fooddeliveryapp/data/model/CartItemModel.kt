package com.example.fooddeliveryapp.data.model

data class CartItemModel(
    val cartImage: Int,
    val cartFoodName: String,
    val cartFoodPrice: Double,
    val cartFoodSize: String,
    val cartFoodQuantity: Int,
    val restaurantId: Int,
    val cartId: String? = null
)
