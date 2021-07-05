package com.example.ecomode.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class UserSharedPreferencesImpl(sharedPreferences: SharedPreferences) :
    UserSharedPreferences {
    private val prefSubject = BehaviorSubject.createDefault(sharedPreferences)

    private val prefChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            prefSubject.onNext(sharedPreferences)
        }

    companion object {
        private const val PREFERENCE_NAME = "ecomode_preferences"

        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_BUDGET = "budget"

        @JvmStatic
        fun create(context: Context): UserSharedPreferencesImpl {
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return UserSharedPreferencesImpl(preferences)
        }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun getUserInfo(): Observable<User> = prefSubject
        .map {
            User(
                it.getString(KEY_USER_NAME, ""),
                it.getLong(KEY_USER_BUDGET, 0)
            )
        }

    override fun saveUserInfo(userName: String, budget: Long): Completable =
        prefSubject
            .firstOrError()
            .flatMapCompletable {
                Completable.fromAction {
                    it.edit().putString(KEY_USER_NAME, userName).apply()
                    it.edit().putLong(KEY_USER_BUDGET, budget).apply()
                }
            }

}