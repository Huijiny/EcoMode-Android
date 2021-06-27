package com.example.ecomode.data.sharedpreferences

import io.reactivex.Completable
import io.reactivex.Observable

interface EcoModeSharedPreferences {
    fun getUserInfo(): Observable<User>

    fun saveUserInfo(userName: String, budget: Long): Completable
}