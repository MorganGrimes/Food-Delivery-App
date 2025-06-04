package com.example.fooddeliveryapp.ui.address

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.local.entity.AddressEntity
import com.example.fooddeliveryapp.data.local.database.AppDatabase
import com.example.fooddeliveryapp.data.repository.AddressRepository
import kotlinx.coroutines.launch

class AddressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AddressRepository

    val allAddresses: LiveData<List<AddressEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).addressDao()
        repository = AddressRepository(dao)
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
