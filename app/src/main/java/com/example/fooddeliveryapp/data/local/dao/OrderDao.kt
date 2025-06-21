package com.example.fooddeliveryapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddeliveryapp.data.local.entity.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): LiveData<List<OrderEntity>>

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}
