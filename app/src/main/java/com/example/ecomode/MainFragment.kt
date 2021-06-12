
package com.example.ecomode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import com.example.ecomode.databinding.FragmentMainBinding
import com.google.android.material.appbar.AppBarLayout

class MainFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentMainBinding? = null
    val binding get()= _binding!!

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 100

    private var isTitleVisible = false
    private var isTitleDetailVisible = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = ""
        binding.appBarLayout.addOnOffsetChangedListener(this)

        startAlphaAnimation(binding.toolbarTitle, 0, View.INVISIBLE)
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
                startAlphaAnimation(binding.toolbarTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                isTitleVisible = true
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(binding.toolbarTitle, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                isTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnAppbarDetail(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleDetailVisible) {
                startAlphaAnimation(binding.toolbarDetail, ALPHA_ANIMATIONS_DURATION.toLong(), View.INVISIBLE)
                isTitleDetailVisible = false
            }
        } else {
            if (!isTitleDetailVisible) {
                startAlphaAnimation(binding.toolbarDetail, ALPHA_ANIMATIONS_DURATION.toLong(), View.VISIBLE)
                isTitleDetailVisible = true
            }
        }
    }

    private fun startAlphaAnimation (view: View, duration: Long, visibility: Int) {
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


}