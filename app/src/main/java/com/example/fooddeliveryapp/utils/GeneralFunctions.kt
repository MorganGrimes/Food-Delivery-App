package com.example.fooddeliveryapp.utils

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.fooddeliveryapp.R

fun Fragment.onBackPressed() {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        })
}

fun Fragment.setupDialogMargins(view: View) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = resources.getDimensionPixelSize(R.dimen.margin_14dp)
    layoutParams.marginEnd = resources.getDimensionPixelSize(R.dimen.margin_14dp)
    view.layoutParams = layoutParams
}