package com.example.ecomode

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecomode.data.repository.UserRepository
import com.example.ecomode.data.room.database.SpendingDatabase
import com.example.ecomode.databinding.FragmentProfileBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class ProfileFragment : Fragment() {

    private val disposables by lazy { CompositeDisposable() }

    private val profileViewModel by viewModels<ProfileViewModel> {
        object : ViewModelProvider.Factory {
            private val database by lazy { SpendingDatabase.getDatabase(requireContext()) }
            private val repository by lazy { UserRepository(database) }

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ProfileViewModel(repository) as T
            }
        }
    }

    private var budgetResult = ""

    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener { navigateToMain() }
        binding.completedButton.setOnClickListener { profileViewModel.insertUserInformation() }
        onBindViewModel()
        setTextWatcher()
    }


    @SuppressLint("LongLogTag")
    private fun onBindViewModel() {
        profileViewModel.isUserInfoCompletedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.completedButton.isEnabled = it
            }, {
                Log.e("User information input is all filled", it.localizedMessage)
            })
            .addToDisposables()

        profileViewModel.isInsertUserCompletedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isInserted ->
                if (isInserted) {
                    navigateToMain()
                }
            }, {
                Log.e("User Information doesn't inserted", it.localizedMessage)
            })
            .addToDisposables()
    }

    private fun setTextWatcher() {
        binding.nameFieldInput.doOnTextChanged { text, _, _, _ ->
            profileViewModel.setUserName(text.toString())
        }

        binding.budgetFieldInput.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotBlank() && text.toString() != budgetResult) {
                val budget = text.toString().replace(",", "")
                profileViewModel.setBudget(budget.toLong())
                budgetResult = String.format("%,d", budget.toLongOrNull() ?: 0L)
                binding.budgetFieldInput.setText(budgetResult)
                binding.budgetFieldInput.setSelection(budgetResult.length)
            }
        }
    }

    private fun navigateToMain() =
        findNavController().navigate(R.id.action_profileFragment_to_mainFragment)

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    fun Disposable.addToDisposables(): Disposable = addTo(disposables)
}