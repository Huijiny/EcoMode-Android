package com.example.ecomode.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecomode.data.room.entity.UserEntity
import com.example.ecomode.data.sharedpreferences.EcoModeSharedPreferencesImpl
import com.example.ecomode.data.sharedpreferences.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MeViewModel(private val sharedPreferencesImpl: EcoModeSharedPreferencesImpl): ViewModel() {
    private val disposables by lazy { CompositeDisposable() }
    private val _userInfoInputSubject: BehaviorSubject<MeViewModel.User> =
        BehaviorSubject.createDefault(MeViewModel.User())

    private val _isUserInfoCompletedSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    val isUserInfoCompletedSubject: Observable<Boolean> = _isUserInfoCompletedSubject

    private val _isInsertUserCompletedSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    val isInsertUserCompletedSubject: Observable<Boolean> = _isInsertUserCompletedSubject

    private val _userInfoGetSubject: BehaviorSubject<User> = BehaviorSubject.createDefault(User())


    init {
        _userInfoInputSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userInput ->
                _isUserInfoCompletedSubject.onNext(userInput.isValidate())
            }, {
                Log.e("Rx Error Logger", it.localizedMessage)
            })
            .addToDisposables()
    }

    fun setUserName(userName: String) {
        _userInfoInputSubject.onNext(
            _userInfoInputSubject.value?.copy(userName = userName)
                ?: MeViewModel.User(userName = userName)
        )
    }

    fun setBudget(budget: Long) {
        _userInfoInputSubject.onNext(
            _userInfoInputSubject.value?.copy(budget = budget)
                ?: MeViewModel.User(budget = budget)
        )
    }

    fun insertUserInformation() {
        _userInfoInputSubject.firstOrError()
            .subscribeOn(Schedulers.io())
            .subscribe({ user ->
                if (!user.userName.isNullOrBlank() && user.budget != null) {
                    sharedPreferencesImpl.saveUserInfo(user.userName, user.budget)
                        .subscribe { _isInsertUserCompletedSubject.onNext(true) }
                        .addToDisposables()
                }
            }, {
                Log.e("Rx Error insertingUser", it.localizedMessage)
            })
            .addToDisposables()
    }

    fun getUserInformation(): Observable<com.example.ecomode.data.sharedpreferences.User> {
        return sharedPreferencesImpl.getUserInfo()
    }


    override fun onCleared() {
        disposables.clear()
    }

    fun Disposable.addToDisposables(): Disposable = addTo(disposables)

    data class User(
        val userName: String? = null,
        val budget: Long? = null
    )

    private fun MeViewModel.User.isValidate(): Boolean =
        !userName.isNullOrBlank() && budget != null
}