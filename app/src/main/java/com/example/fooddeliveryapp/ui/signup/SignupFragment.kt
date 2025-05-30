package com.example.fooddeliveryapp.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
            signUpBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }

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
            val fields = listOf(
                signUpNameEt,
                signUpEmailEt,
                signUpPasswordEt,
                signUpRetypePasswordEt
            )

            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val allFieldsFilled = fields.all { it.text?.isNotBlank() == true }

                    signUpBtn.isEnabled = allFieldsFilled
                    signUpBtn.setBackgroundColor(
                        if (allFieldsFilled) requireContext().getColor(R.color.orange)
                        else UiUtils.brownColor
                    )
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            fields.forEach { it.addTextChangedListener(textWatcher) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}