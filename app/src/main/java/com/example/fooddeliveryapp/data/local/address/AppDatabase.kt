package com.example.fooddeliveryapp.data.local.address

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AddressEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun addressDao(): AddressDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "food_delivery_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
