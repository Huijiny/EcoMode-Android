package com.example.ecomode.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomode.MainAdapter
import com.example.ecomode.R
import com.example.ecomode.data.sharedpreferences.UserSharedPreferencesImpl
import com.example.ecomode.databinding.FragmentMainBinding
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val disposables by lazy { CompositeDisposable() }

    private val meViewModel by viewModels<MeViewModel> {
        object : ViewModelProvider.Factory {
            private val repository by lazy { UserSharedPreferencesImpl.create(requireContext())}

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MeViewModel(repository) as T
            }
        }
    }

    companion object {
        private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private val ALPHA_ANIMATIONS_DURATION = 100
    }


    private var isTitleVisible = false
    private var isTitleDetailVisible = true

    private lateinit var mainAdapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayout.addOnOffsetChangedListener(this)
        binding.appbar.currentDate.text =
            SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().time)
        startAlphaAnimation(binding.appbar.toolbarTitle, 0, View.INVISIBLE)
        binding.mainRecycler.setup()
        toolbarSetup()

        if (mainAdapter.spendingData.size == 0) {
            binding.mainRecycler.visibility = View.GONE
            binding.defaultView.visibility = View.VISIBLE
        } else {
            binding.mainRecycler.visibility = View.VISIBLE
            binding.defaultView.visibility = View.GONE
        }
        onBindViewModel()
    }

    private fun onBindViewModel() {
        meViewModel.getUserInformation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.appbar.name.text = it.userName
                binding.appbar.budget.text = it.budget.toString()
                binding.appbar.toolbarTitle.text = it.budget.toString()
            }, {
                Log.e("getUserInformationError", it.message.orEmpty())
            })
            .addToDisposable()
    }

    private fun toolbarSetup() {
        binding.appbar.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile -> {
                    navigateToProfile()
                    true
                }
                else -> false

            }
        }
    }

    private fun RecyclerView.setup() {
        mainAdapter = MainAdapter()
        binding.mainRecycler.adapter = mainAdapter
        binding.mainRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.totalScrollRange?.let {
            val percentage = Math.abs(verticalOffset).toFloat() / it.toFloat()
            handleAlphaOnAppbarDetail(percentage)
            handleToolbarTitleVisibility(percentage)
        }
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTitleVisible) {
                startAlphaAnimation(
                    binding.appbar.toolbarTitle,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE
                )
                isTitleVisible = true
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(
                    binding.appbar.toolbarTitle,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE
                )
                isTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnAppbarDetail(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleDetailVisible) {
                startAlphaAnimation(
                    binding.appbar.toolbarDetail,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.INVISIBLE
                )
                isTitleDetailVisible = false
            }
        } else {
            if (!isTitleDetailVisible) {
                startAlphaAnimation(
                    binding.appbar.toolbarDetail,
                    ALPHA_ANIMATIONS_DURATION.toLong(),
                    View.VISIBLE
                )
                isTitleDetailVisible = true
            }
        }
    }

    private fun startAlphaAnimation(view: View, duration: Long, visibility: Int) {
        if (visibility == View.VISIBLE) {
            AlphaAnimation(0f, 1f).also {
                it.duration = duration
                it.fillAfter = true
                view.startAnimation(it)
            }
        } else {
            AlphaAnimation(1f, 0f).also {
                it.duration = duration
                it.fillAfter = true
                view.startAnimation(it)
            }
        }

    }

    private fun navigateToProfile() =
        findNavController().navigate(R.id.action_mainFragment_to_profileFragment)

    private fun Disposable.addToDisposable(): Disposable = addTo(disposables)
}