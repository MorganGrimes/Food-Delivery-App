package com.example.fooddeliveryapp.data.repository

import androidx.lifecycle.LiveData
import com.example.fooddeliveryapp.data.local.dao.AddressDao
import com.example.fooddeliveryapp.data.local.entity.AddressEntity

class AddressRepository (private val addressDao: AddressDao) {

    val allAddresses: LiveData<List<AddressEntity>> = addressDao.getAllAddresses()

    suspend fun insert(address: AddressEntity) {
        addressDao.insertAddress(address)
    }

    suspend fun update(address: AddressEntity) {
        addressDao.updateAddress(address)
    }

    suspend fun delete(address: AddressEntity) {
        addressDao.deleteAddress(address)
    }
}