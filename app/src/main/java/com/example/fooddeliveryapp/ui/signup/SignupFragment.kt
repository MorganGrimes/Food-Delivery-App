package com.example.fooddeliveryapp.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentSignupBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import com.example.fooddeliveryapp.utils.UiUtils

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupInputValidation()
    }

    private fun setupListener() {
        binding.apply {

            signUpBtn.apply {
                isEnabled = false
                setBackgroundColor(UiUtils.brownColor)
            }

            signUpBtn.setOnClickListener {
                val name = signUpNameEt.text.toString().trim()
                val email = signUpEmailEt.text.toString().trim()
                val password = signUpPasswordEt.text.toString()
                val retypePassword = signUpRetypePasswordEt.text.toString()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all registration fields",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (password != retypePassword) {
                    Toast.makeText(
                        requireContext(),
                        "The passwords do not matches",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                ProfileSharedPreferences.saveUserData(requireContext(), name, email, password)
                Toast.makeText(requireContext(), "Registration Complete!", Toast.LENGTH_SHORT)
                    .show()
                it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            }
        }
    }

    private fun setupInputValidation() {
        binding.apply {
            val nameField = signUpNameEt
            val emailField = signUpEmailEt
            val passwordField = signUpPasswordEt
            val retypePasswordField = signUpRetypePasswordEt
            val signUpButton = signUpBtn

            val textWatcher = object : android.text.TextWatcher {
                override fun afterTextChanged(s: android.text.Editable?) {
                    val name = nameField.text.toString().trim()
                    val email = emailField.text.toString().trim()
                    val password = passwordField.text.toString()
                    val retypePassword = retypePasswordField.text.toString()

                    val isFormFilled = name.isNotEmpty() &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            retypePassword.isNotEmpty()

                    signUpButton.isEnabled = isFormFilled
                    signUpButton.setBackgroundColor(
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

            nameField.addTextChangedListener(textWatcher)
            emailField.addTextChangedListener(textWatcher)
            passwordField.addTextChangedListener(textWatcher)
            retypePasswordField.addTextChangedListener(textWatcher)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}