package com.example.fooddeliveryapp.data.model

data class CreditCardItemModel(
    val creditCardName: String,
    val creditCardImage: Int,
    val creditCardLastNumbers: String,
    val creditCardHolderName: String,
    val creditCardExpireDate: String,
    val creditCardCvc: String,
    var isExpanded: Boolean = false
    )
