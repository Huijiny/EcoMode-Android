package com.example.ecomode.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecomode.data.sharedpreferences.User
import com.example.ecomode.data.sharedpreferences.UserSharedPreferencesImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MeViewModel(private val sharedPreferencesImpl: UserSharedPreferencesImpl): ViewModel() {
    private val disposables by lazy { CompositeDisposable() }
    private val _userInfoInputSubject: BehaviorSubject<User> =
        BehaviorSubject.createDefault(User())

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
                ?: User(userName = userName)
        )
    }

    fun setBudget(budget: Long) {
        _userInfoInputSubject.onNext(
            _userInfoInputSubject.value?.copy(budget = budget)
                ?: User(budget = budget)
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

    fun getUserInformation(): Observable<User> {
        return sharedPreferencesImpl.getUserInfo()
    }


    override fun onCleared() {
        disposables.clear()
    }

    fun Disposable.addToDisposables(): Disposable = addTo(disposables)

    private fun User.isValidate(): Boolean =
        !userName.isNullOrBlank() && budget != null
}