package com.example.fooddeliveryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.fooddeliveryapp.utils.CREDIT_CARDS

@Entity(tableName = CREDIT_CARDS)
data class CreditCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val creditCardName: String,
    val creditCardImage: Int,
    val creditCardNumbers: String,
    val creditCardHolderName: String,
    val creditCardExpireDate: String,
    val creditCardCvc: String,
    var isExpanded: Boolean = false,
    var balance: Double
)
