package com.example.fooddeliveryapp.data.remote.dto.restaurant

data class Coupon(
    val code: String,
    val discountPercentage: Int,
    val startDate: String,
    val endDate: String,
    val products: List<String>
)