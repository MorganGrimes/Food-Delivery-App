package com.example.fooddeliveryapp.data.local.address

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val addressLabel: String,
    val addressName: String,
    val addressStreet: String,
    val addressPostCode: String,
    val addressApartment: String
)

