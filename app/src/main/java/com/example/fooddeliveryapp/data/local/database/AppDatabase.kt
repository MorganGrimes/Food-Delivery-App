package com.example.fooddeliveryapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fooddeliveryapp.data.local.dao.AddressDao
import com.example.fooddeliveryapp.data.local.dao.CreditCardDao
import com.example.fooddeliveryapp.data.local.dao.OrderDao
import com.example.fooddeliveryapp.data.local.entity.AddressEntity
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity
import com.example.fooddeliveryapp.data.local.entity.OrderEntity
import com.example.fooddeliveryapp.utils.FOOD_DELIVERY_DB

@Database(
    entities = [AddressEntity::class, CreditCardEntity::class, OrderEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao
    abstract fun creditCardDao(): CreditCardDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    FOOD_DELIVERY_DB
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
