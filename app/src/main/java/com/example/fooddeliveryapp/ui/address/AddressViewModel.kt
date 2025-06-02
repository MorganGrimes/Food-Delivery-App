package com.example.fooddeliveryapp.ui.address

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.local.address.AddressEntity
import com.example.fooddeliveryapp.data.local.address.AppDatabase
import com.example.fooddeliveryapp.data.repository.AppRepository
import kotlinx.coroutines.launch

class AddressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    val allAddresses: LiveData<List<AddressEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).addressDao()
        repository = AppRepository(dao)
        allAddresses = repository.allAddresses
    }

    fun insert(address: AddressEntity) = viewModelScope.launch {
        repository.insert(address)
    }

    fun update(address: AddressEntity) = viewModelScope.launch {
        repository.update(address)
    }

    fun delete(address: AddressEntity) = viewModelScope.launch {
        repository.delete(address)
    }
}
