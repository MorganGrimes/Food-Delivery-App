package com.example.fooddeliveryapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity

@Dao
interface CreditCardDao {

    @Query("SELECT * FROM CREDIT_CARDS")
    fun getAllCards(): LiveData<List<CreditCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(creditCard: CreditCardEntity)

    @Update
    suspend fun updateCard(creditCard: CreditCardEntity)

    @Delete
    suspend fun deleteCard(creditCard: CreditCardEntity)
}
