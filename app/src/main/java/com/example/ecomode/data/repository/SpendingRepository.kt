package com.example.ecomode.data.repository

import androidx.annotation.WorkerThread
import com.example.ecomode.data.room.dao.SpendingDao
import com.example.ecomode.data.room.entity.SpendingEntity

class SpendingRepository(private val spendingDao: SpendingDao, private val date: String) {

    val spendingsInMonth: List<SpendingEntity> = spendingDao.getSpendingsInMonth(date)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(spending: SpendingEntity) {
        spendingDao.insertSpending(spending)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(spending: SpendingEntity) {
        spendingDao.updateSpending(spending)
    }

}