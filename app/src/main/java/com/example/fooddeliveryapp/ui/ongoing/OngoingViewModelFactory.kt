package com.example.fooddeliveryapp.ui.ongoing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fooddeliveryapp.data.repository.OrderRepository

class OngoingViewModelFactory(
    private val repository: OrderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OngoingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OngoingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
