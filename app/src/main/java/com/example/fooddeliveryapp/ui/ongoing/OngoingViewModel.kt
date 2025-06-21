package com.example.fooddeliveryapp.ui.ongoing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.local.entity.OrderEntity
import com.example.fooddeliveryapp.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OngoingViewModel(private val repository: OrderRepository): ViewModel() {

    val allOrders = repository.allOrders

    fun insertOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.insertOrder(order)
        }
    }

    fun clearOrders() {
        viewModelScope.launch {
            repository.clearOrders()
        }
    }
}