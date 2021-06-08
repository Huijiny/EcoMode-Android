package com.example.ecomode.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class UserEntity(
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "budget")
    val budget: Long,
)
