package com.example.ecomode.data.repository

import android.content.Context
import com.example.ecomode.data.room.dao.UserDao
import com.example.ecomode.data.room.database.SpendingDatabase
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserRepository(context:Context) {
    private var userDao: UserDao

    init {
        val databse = SpendingDatabase.getDatabase(context)
        userDao = databse.userDao()
    }

    suspend fun insertUser(user: UserEntity):Completable {
        return userDao.setUserInfo(userEntity = user)
    }

    fun getUser(): Flowable<UserEntity> {
        return userDao.getUserInfo()
    }
}