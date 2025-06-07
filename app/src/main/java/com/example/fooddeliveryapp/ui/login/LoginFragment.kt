package com.example.fooddeliveryapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentLoginBinding
import com.example.fooddeliveryapp.utils.ENTER_YOUR_EMAIL
import com.example.fooddeliveryapp.utils.ENTER_YOUR_PSW
import com.example.fooddeliveryapp.utils.FOOD_PREFS
import com.example.fooddeliveryapp.utils.LOGIN
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import com.example.fooddeliveryapp.utils.REMEMBER_ME
import com.example.fooddeliveryapp.utils.SAVED_EMAIL
import com.example.fooddeliveryapp.utils.SAVED_PSW
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.WRONG_EMAIL_PSW
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var callbackManager: CallbackManager

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
        facebookLogin()
    }

    private fun setupListener() {
        callbackManager = CallbackManager.Factory.create()
        val navController = findNavController()
        binding.apply {
            listOf("email")

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
                    loginEmailEt.error = ENTER_YOUR_EMAIL
                    return@setOnClickListener
                }

                if (passwordInput.isEmpty()) {
                    loginPasswordEt.error = ENTER_YOUR_PSW
                    return@setOnClickListener
                }

                val sharedPref =
                    requireActivity().getSharedPreferences(FOOD_PREFS, Context.MODE_PRIVATE)
                val savedEmail = ProfileSharedPreferences.getUserEmail(requireContext())
                val savedPassword = ProfileSharedPreferences.getUserPassword(requireContext())

                if (emailInput == savedEmail && passwordInput == savedPassword) {

                    if (loginRememberCheckbox.isChecked) {
                        loginRememberCheckbox
                        with(sharedPref.edit()) {
                            putBoolean(REMEMBER_ME, true)
                            putString(SAVED_EMAIL, emailInput)
                            putString(SAVED_PSW, passwordInput)
                            apply()
                        }
                    } else {
                        with(sharedPref.edit()) {
                            putBoolean(REMEMBER_ME, false)
                            remove(SAVED_EMAIL)
                            remove(SAVED_PSW)
                            apply()
                        }
                    }

                    Toast.makeText(requireContext(), LOGIN, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), WRONG_EMAIL_PSW, Toast.LENGTH_SHORT)
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

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            emailField.addTextChangedListener(textWatcher)
            passwordField.addTextChangedListener(textWatcher)
        }
    }

    private fun checkRememberMe() {
        binding.apply {
            val sharedPref =
                requireActivity().getSharedPreferences(FOOD_PREFS, Context.MODE_PRIVATE)
            val remember = sharedPref.getBoolean(REMEMBER_ME, false)

            if (remember) {
                loginEmailEt.setText(ProfileSharedPreferences.getUserEmail(requireContext()))
                loginPasswordEt.setText(ProfileSharedPreferences.getUserPassword(requireContext()))
                loginRememberCheckbox.isChecked = true
            }
        }
    }

    private fun facebookLogin() {

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.i("FBLogin", "Login success: ${result.accessToken.token}")
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }

                override fun onCancel() {
                    Log.i("FBLogin", "Login canceled")
                }

                override fun onError(error: FacebookException) {
                    Log.i("FBLogin", "Login error: ${error.message}", error)
                }
            })

        binding.facebookIconIv.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this@LoginFragment,
                listOf("email", "public_profile")
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
