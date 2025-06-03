package com.example.fooddeliveryapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fooddeliveryapp.data.local.entity.AddressEntity

@Dao
interface AddressDao {

    @Query("SELECT * FROM addresses")
    fun getAllAddresses(): LiveData<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)
}
