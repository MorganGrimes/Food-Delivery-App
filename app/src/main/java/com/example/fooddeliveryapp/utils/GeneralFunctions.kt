package com.example.fooddeliveryapp.utils

import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.fooddeliveryapp.R
import java.util.Locale

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

fun getDrawableForCategory(category: String): Int {
    return when (category.lowercase(Locale.getDefault())) {
        "kebab" -> R.drawable.kebab
        "pizza" -> R.drawable.pizza
        "burger" -> R.drawable.burger
        "sushi" -> R.drawable.sushi
        "hotdog" -> R.drawable.hotdog
        else -> R.drawable.ic_launcher_background
    }
}