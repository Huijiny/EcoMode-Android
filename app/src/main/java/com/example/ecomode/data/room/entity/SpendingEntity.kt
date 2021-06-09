package com.example.ecomode.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "spending")
data class SpendingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "spending_name")
    val name: String,
    @ColumnInfo(name = "spending_amount")
    val amount: Long,
    @ColumnInfo(name = "spending_date")
    val date: Date
)
