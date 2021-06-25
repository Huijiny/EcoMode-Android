package com.example.ecomode.data.repository

import android.app.Application
import android.content.Context
import com.example.ecomode.data.room.dao.UserDao
import com.example.ecomode.data.room.database.SpendingDatabase
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class UserRepository(val database: SpendingDatabase) {
    private var userDao: UserDao

    init {
        userDao = database.userDao()
    }

    fun insertUser(userName: String, budget: Long): Completable {
        return userDao.setUserInfo(
            UserEntity(
            username = userName, budget = budget
        ))
    }

    fun getUser(): Flowable<UserEntity> {
        return userDao.getUserInfo()
    }
}