package com.example.fooddeliveryapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAuthBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.linearLyCodeBox.visibility = View.GONE
        binding.resendTv.visibility = View.GONE
        binding.authExampleEmailTv.visibility = View.GONE

        setupListener()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupListener() {
        var isCodeSent = false
        val inputEmail = binding.authEmailEt.text.toString().trim()
        binding.authBtn.setOnClickListener {
            if (!isCodeSent) {

                if (inputEmail.isNotEmpty() && inputEmail == ProfileSharedPreferences.getUserEmail(
                        requireContext()
                    )
                ) {
                    val otp = generateOTP()
                    ProfileSharedPreferences.saveOTP(requireContext(), otp)
                    isCodeSent = true
                    showCodeInput()
                    Toast.makeText(
                        requireContext(),
                        "Code sent to $inputEmail",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Enter a valid Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                val inputCode = getInputOTP()
                val savedCode = ProfileSharedPreferences.getSavedOTP(requireContext())

                if (inputCode == savedCode) {
                    Toast.makeText(requireContext(), "Correct Code!", Toast.LENGTH_SHORT).show()
                    it.findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireContext(), "Wrong Code", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.resendTv.setOnClickListener {
            val email =
                ProfileSharedPreferences.getUserEmail(requireContext()) ?: return@setOnClickListener
            val otp = generateOTP()
            ProfileSharedPreferences.saveOTP(requireContext(), otp)

            Log.d("AuthFragment", "New OTP: $otp")
            Toast.makeText(requireContext(), "New code sent to $email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateOTP(): String {
        return (1000..9999).random().toString()
    }

    private fun getInputOTP(): String {
        val first = binding.firstPinEt.text.toString()
        val second = binding.secondPinEt.text.toString()
        val third = binding.thirdPinEt.text.toString()
        val fourth = binding.fourthPinEt.text.toString()

        return first + second + third + fourth
    }

    private fun showCodeInput() {
        binding.linearLyCodeBox.visibility = View.VISIBLE
        binding.resendTv.visibility = View.VISIBLE
        binding.authExampleEmailTv.visibility = View.VISIBLE

        binding.authEmailEt.visibility = View.GONE
        binding.authEmailOrCodeTv.text = getString(R.string.code)
        binding.authBtn.text = getString(R.string.verify)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}