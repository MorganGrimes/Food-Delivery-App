package com.example.fooddeliveryapp.data.remote.dto.restaurant

data class Restaurants(
    val id: Int,
    val name: String,
    val locationName: String,
    val locationCoordinates: LocationCoordinates,
    val rating: Double,
    val delivery: String,
    val deliveryTime: String,
    val food: Map<String, List<FoodItem>>
)

