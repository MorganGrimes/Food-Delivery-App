package com.example.fooddeliveryapp.data.repository

import androidx.lifecycle.LiveData
import com.example.fooddeliveryapp.data.local.dao.CreditCardDao
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity

class CreditCardRepository  (private val creditCardDao: CreditCardDao) {

    val allCreditCards: LiveData<List<CreditCardEntity>> = creditCardDao.getAllCards()

    suspend fun insertCard(card: CreditCardEntity) {
        creditCardDao.insertCard(card)
    }

    suspend fun updateCard(card: CreditCardEntity) {
        creditCardDao.updateCard(card)
    }

    suspend fun deleteCard(card: CreditCardEntity) {
        creditCardDao.deleteCard(card)
    }
}