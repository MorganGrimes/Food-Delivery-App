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
import com.example.fooddeliveryapp.utils.UiUtils

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
        setupInputValidation()
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

    private fun setupInputValidation() {
        binding.apply {
        val emailField = loginEmailEt
        val passwordField = loginPasswordEt
        val loginButton = loginBtn

        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val email = emailField.text.toString().trim()
                val password = passwordField.text.toString()
                val isFormFilled = email.isNotEmpty() && password.isNotEmpty()

                loginButton.isEnabled = isFormFilled
                loginButton.setBackgroundColor(
                    if (isFormFilled) requireContext().getColor(R.color.orange) else UiUtils.brownColor
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        emailField.addTextChangedListener(textWatcher)
        passwordField.addTextChangedListener(textWatcher)
        }
    }

    private fun checkRememberMe() {
        binding.apply {
        val sharedPref = requireActivity().getSharedPreferences("food_prefs", Context.MODE_PRIVATE)
        val remember = sharedPref.getBoolean("rememberMe", false)

        if (remember) {
            loginEmailEt.setText(ProfileSharedPreferences.getUserEmail(requireContext()))
            loginPasswordEt.setText(ProfileSharedPreferences.getUserPassword(requireContext()))
            loginRememberCheckbox.isChecked = true
        }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
