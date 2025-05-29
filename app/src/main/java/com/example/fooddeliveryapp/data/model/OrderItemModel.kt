package com.example.fooddeliveryapp.data.model

data class OrderItemModel(
    val orderCategoryTypeName: String,
    val orderStatus: String,
    val orderImage: Int,
    val orderSellerName: String,
    val orderPrice: String,
    val orderDate: String,
    val orderItemNumber: String,
    val orderId: String
)