package com.example.fooddeliveryapp.data.model

data class RestaurantsItemModel(
    val restaurantImage: Int,
    val restaurantId: Int,
    val restaurantName: String,
    val restaurantDescription: String,
    val restaurantFood: String,
    val restaurantRating: String,
    val restaurantDelivery: String,
    val restaurantDeliveryTime: String
)
