package com.example.fooddeliveryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderCategoryTypeName: String,
    val orderStatus: String,
    val orderImage: Int,
    val orderSellerName: String,
    val orderPrice: String,
    val orderDate: String,
    val orderItemNumber: String,
    val orderId: String
)
