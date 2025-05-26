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
    }

    private fun setupListener() {
        binding.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}