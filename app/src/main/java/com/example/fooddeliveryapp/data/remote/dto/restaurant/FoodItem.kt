package com.example.fooddeliveryapp.data.remote.dto.restaurant

data class FoodItem(
    val name: String,
    val size: List<String>,
    val ingredients: List<String>,
    val price: Double,
    val availableQuantity: Int,
    val eatenLastMonth: Int,
    val description: String
)

