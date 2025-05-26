package com.example.fooddeliveryapp.ui.splash

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            val navController = findNavController()
            if (ProfileSharedPreferences.isOnboardingCompleted(requireContext())) {
                navController.navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                navController.navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }
    }
}