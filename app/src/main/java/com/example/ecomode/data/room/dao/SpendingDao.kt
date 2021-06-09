package com.example.ecomode.data.room.dao

import androidx.room.*
import com.example.ecomode.data.room.entity.SpendingEntity

@Dao
interface SpendingDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpending(spendingEntity: SpendingEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateSpending(spendingEntity: SpendingEntity)

    @Query("SELECT * FROM spending WHERE strftime('%Y-%m', spending_date) = :date")
    fun getSpendingsInMonth(date: String): List<SpendingEntity>

    @Query("DELETE FROM spending WHERE id = :id")
    suspend fun deleteSpending(id: Long)
}