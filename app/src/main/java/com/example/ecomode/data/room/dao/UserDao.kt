package com.example.ecomode.data.room.dao

import androidx.room.*
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserInfo(): Flowable<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserInfo(userEntity: UserEntity): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserInfo(userEntity: UserEntity): Completable
}