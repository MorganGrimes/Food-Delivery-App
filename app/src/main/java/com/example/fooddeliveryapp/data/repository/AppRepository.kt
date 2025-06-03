package com.example.fooddeliveryapp.data.repository

import androidx.lifecycle.LiveData
import com.example.fooddeliveryapp.data.local.dao.AddressDao
import com.example.fooddeliveryapp.data.local.entity.AddressEntity

class AppRepository (private val dao: AddressDao) {

    val allAddresses: LiveData<List<AddressEntity>> = dao.getAllAddresses()

    suspend fun insert(address: AddressEntity) {
        dao.insertAddress(address)
    }

    suspend fun update(address: AddressEntity) {
        dao.updateAddress(address)
    }

    suspend fun delete(address: AddressEntity) {
        dao.deleteAddress(address)
    }
}