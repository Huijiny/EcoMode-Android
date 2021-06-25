package com.example.ecomode.data.room.dao

import androidx.room.*
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUserInfo(): Flowable<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setUserInfo(userEntity: UserEntity): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUserInfo(userEntity: UserEntity): Completable
}