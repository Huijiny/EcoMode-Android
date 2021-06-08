package com.example.ecomode.room.dao

import androidx.room.*
import com.example.ecomode.room.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserInfo(): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserInfo(userEntity: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserInfo(userEntity: UserEntity)
}