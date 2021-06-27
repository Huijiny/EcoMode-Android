package com.example.ecomode

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecomode.data.repository.UserRepository
import com.example.ecomode.data.room.entity.UserEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }
    private val _userInfoInputSubject: BehaviorSubject<User> =
        BehaviorSubject.createDefault(User())

    private val _isUserInfoCompletedSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    val isUserInfoCompletedSubject: Observable<Boolean> = _isUserInfoCompletedSubject

    private val _isInsertUserCompletedSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    val isInsertUserCompletedSubject: Observable<Boolean> = _isInsertUserCompletedSubject


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
                    userRepository.insertUser(UserEntity(user.userName, user.budget))
                        .subscribe { _isInsertUserCompletedSubject.onNext(true) }
                        .addToDisposables()
                }
            }, {
                Log.e("Rx Error insertingUser", it.localizedMessage)
            })
            .addToDisposables()
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun Disposable.addToDisposables(): Disposable = addTo(disposables)


    data class User(
        val userName: String? = null,
        val budget: Long? = null
    )

    private fun User.isValidate(): Boolean =
        !userName.isNullOrBlank() && budget != null
}
