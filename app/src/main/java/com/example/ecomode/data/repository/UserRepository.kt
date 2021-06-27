package com.example.ecomode.data.repository

import com.example.ecomode.data.room.dao.UserDao
import com.example.ecomode.data.room.database.SpendingDatabase
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable

class UserRepository(val database: SpendingDatabase) {
    private var userDao: UserDao

    init {
        userDao = database.userDao()
    }

    fun insertUser(userEntity: UserEntity): Completable {
        return database.userDao().setUserInfo(userEntity)
    }

    fun getUser(): Flowable<UserEntity> {
        return userDao.getUserInfo()
    }
}