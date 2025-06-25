package com.example.fooddeliveryapp.data.repository

import androidx.lifecycle.LiveData
import com.example.fooddeliveryapp.data.local.dao.OrderDao
import com.example.fooddeliveryapp.data.local.entity.OrderEntity

class OrderRepository(private val orderDao: OrderDao) {

    val allOrders: LiveData<List<OrderEntity>> = orderDao.getAllOrders()

    fun getOngoingOrders(): LiveData<List<OrderEntity>> {
        val twentyMinutesAgo = System.currentTimeMillis() - 20 * 60 * 1000
        return orderDao.getOngoingOrders(twentyMinutesAgo)
    }

    fun getHistoryOrders(): LiveData<List<OrderEntity>> {
        val twentyMinutesAgo = System.currentTimeMillis() - 20 * 60 * 1000
        return orderDao.getHistoryOrders(twentyMinutesAgo)
    }

    suspend fun insertOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    suspend fun clearOrders() {
        orderDao.deleteAllOrders()
    }
}