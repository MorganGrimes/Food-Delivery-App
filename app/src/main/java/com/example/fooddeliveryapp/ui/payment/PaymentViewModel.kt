package com.example.fooddeliveryapp.ui.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.local.database.AppDatabase
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity
import com.example.fooddeliveryapp.data.repository.CreditCardRepository
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CreditCardRepository

    val allCreditCards: LiveData<List<CreditCardEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).creditCardDao()
        repository = CreditCardRepository(dao)
        allCreditCards = repository.allCreditCards
    }

    fun insert(creditCard: CreditCardEntity) = viewModelScope.launch {
        repository.insertCard(creditCard)
    }

    fun update(creditCard: CreditCardEntity) = viewModelScope.launch {
        repository.updateCard(creditCard)
    }

    fun delete(creditCard: CreditCardEntity) = viewModelScope.launch {
        repository.deleteCard(creditCard)
    }
}
