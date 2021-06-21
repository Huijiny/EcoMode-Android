package com.example.ecomode

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ecomode.data.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

// User action
// 1. 이름을 입력한다
// 2. 버젯을 입력한다
// 3. 완료 버튼을 누른다

// 모델과 처리해야 할 일
// 1. User 모두 입력 됐을 때, 수정한다.
// 2. User 모두 입력 됐을 때, 생성한다.

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val isEdit: Boolean
) : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }

    private val _userInfoInputSubject: BehaviorSubject<User> =
        BehaviorSubject.createDefault(User())

    private val _isUserInfoCompletedSubject: BehaviorSubject<Boolean> =
        BehaviorSubject.createDefault(false)
    val isUserInfoCompletedSubject: Observable<Boolean> = _isUserInfoCompletedSubject


    init {
        _userInfoInputSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ userInput ->
                _isUserInfoCompletedSubject.onNext(userInput.isValidate())
            },
                {
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

    fun setBudget(budget: Int) {
        _userInfoInputSubject.onNext(
            _userInfoInputSubject.value?.copy(budget = budget)
                ?: User(budget = budget)
        )
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun Disposable.addToDisposables(): Disposable = addTo(disposables)


    data class User(
        val userName: String? = null,
        val budget: Int? = null
    )

    private fun User.isValidate(): Boolean =
        !userName.isNullOrBlank() && !budget.toString().isNullOrBlank()


}
