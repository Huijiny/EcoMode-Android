package com.example.ecomode.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "user")
data class UserEntity(
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "budget")
    val budget: Long,
)
