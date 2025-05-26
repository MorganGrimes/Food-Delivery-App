package com.example.fooddeliveryapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentLoginBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        checkRememberMe()
    }

    private fun setupListener() {
        val navController = findNavController()
        binding.apply {
            loginForgotPasswordTv.setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_authFragment)
            }
            loginSignUpTv.setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_signupFragment)
            }


            loginBtn.setOnClickListener {
                val emailInput = loginEmailEt.text.toString().trim()
                val passwordInput = loginPasswordEt.text.toString()

                if (emailInput.isEmpty()) {
                    loginEmailEt.error = "Enter your Email"
                    return@setOnClickListener
                }

                if (passwordInput.isEmpty()) {
                    loginPasswordEt.error = "Enter your Password"
                    return@setOnClickListener
                }

                val sharedPref =
                    requireActivity().getSharedPreferences("food_prefs", Context.MODE_PRIVATE)
                val savedEmail = ProfileSharedPreferences.getUserEmail(requireContext())
                val savedPassword = ProfileSharedPreferences.getUserPassword(requireContext())

                if (emailInput == savedEmail && passwordInput == savedPassword) {

                    if (loginRememberCheckbox.isChecked) {
                        loginRememberCheckbox
                        with(sharedPref.edit()) {
                            putBoolean("rememberMe", true)
                            putString("savedEmail", emailInput)
                            putString("savedPassword", passwordInput)
                            apply()
                        }
                    } else {
                        with(sharedPref.edit()) {
                            putBoolean("rememberMe", false)
                            remove("savedEmail")
                            remove("savedPassword")
                            apply()
                        }
                    }

                    Toast.makeText(requireContext(), "Login!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), "Wrong Email o Password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun checkRememberMe() {
        val sharedPref = requireActivity().getSharedPreferences("food_prefs", Context.MODE_PRIVATE)
        val remember = sharedPref.getBoolean("rememberMe", false)

        if (remember) {
            binding.loginEmailEt.setText(ProfileSharedPreferences.getUserEmail(requireContext()))
            binding.loginPasswordEt.setText(ProfileSharedPreferences.getUserPassword(requireContext()))
            binding.loginRememberCheckbox.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
