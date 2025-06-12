package com.example.fooddeliveryapp.data.remote.dto

data class RestaurantResponse(
    val id: String,
    val name: String,
    val food: List<String>,
    val rating: Double,
    val delivery: String,
    val deliveryTime: String
)
