package com.example.fooddeliveryapp.data.local.address

import androidx.lifecycle.LiveData
import androidx.room.*

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
