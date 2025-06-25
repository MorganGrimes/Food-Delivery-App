package com.example.fooddeliveryapp.ui.ongoing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.local.entity.OrderEntity
import com.example.fooddeliveryapp.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OngoingViewModel(private val repository: OrderRepository) : ViewModel() {

    val allOrders = repository.allOrders
    val ongoingOrders: LiveData<List<OrderEntity>> = repository.getOngoingOrders()
    val historyOrders: LiveData<List<OrderEntity>> = repository.getHistoryOrders()

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