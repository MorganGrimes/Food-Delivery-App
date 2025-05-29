package com.example.fooddeliveryapp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAuthBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import com.example.fooddeliveryapp.utils.UiUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: Job? = null

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
        binding.apply {

            authBtn.apply {
                isEnabled = false
                setBackgroundColor(UiUtils.brownColor)
            }
            var isCodeSent = false

            authEmailEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s.toString().trim()
                    authBtn.isEnabled = input.isNotEmpty()
                    authBtn.setBackgroundColor(
                        if (input.isNotEmpty()) requireContext().getColor(R.color.orange) else UiUtils.brownColor
                    )
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            authBtn.setOnClickListener {
                if (!isCodeSent) {
                    val inputEmail = authEmailEt.text.toString().trim()

                    if (inputEmail.isNotEmpty() && inputEmail == ProfileSharedPreferences.getUserEmail(
                            requireContext()
                        )
                    ) {
                        val otp = generateOTP()
                        ProfileSharedPreferences.saveOTP(requireContext(), otp)
                        isCodeSent = true
                        showCodeInput()
                        setupPinInputListeners()
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

            resendTv.setOnClickListener {
                startResendTimer()
                val email =
                    ProfileSharedPreferences.getUserEmail(requireContext())
                        ?: return@setOnClickListener
                val otp = generateOTP()
                ProfileSharedPreferences.saveOTP(requireContext(), otp)

                Log.d("AuthFragment", "New OTP: $otp")
                Toast.makeText(requireContext(), "New code sent to $email", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupPinInputListeners() {
        binding.apply {
            authBtn.apply {
                isEnabled = false
                setBackgroundColor(UiUtils.brownColor)
            }

            val pins = listOf(
                firstPinEt,
                secondPinEt,
                thirdPinEt,
                fourthPinEt
            )

            for (i in pins.indices) {
                pins[i].addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (s?.length == 1 && i < pins.lastIndex) {
                            pins[i + 1].requestFocus()
                        } else if (s?.isEmpty() == true && i > 0) {
                            pins[i - 1].requestFocus()
                        }

                        val allFilled = pins.all { it.text?.length == 1 }
                        authBtn.isEnabled = allFilled
                        authBtn.setBackgroundColor(
                            if (allFilled) requireContext().getColor(R.color.orange) else UiUtils.brownColor
                        )
                    }
                })
            }
        }
    }

    private fun generateOTP(): String {
        return (1000..9999).random().toString()
    }

    private fun getInputOTP(): String {
        binding.apply {

            val first = firstPinEt.text.toString()
            val second = secondPinEt.text.toString()
            val third = thirdPinEt.text.toString()
            val fourth = fourthPinEt.text.toString()

            return first + second + third + fourth
        }
    }

    private fun showCodeInput() {
        binding.apply {
            linearLyCodeBox.visibility = View.VISIBLE
            resendTv.visibility = View.VISIBLE
            authExampleEmailTv.visibility = View.VISIBLE

            authEmailEt.visibility = View.GONE
            authEmailOrCodeTv.text = getString(R.string.code)
            authBtn.text = getString(R.string.verify)
        }
    }

    private fun startResendTimer() {
        binding.resendTv.isEnabled = false
        countDownTimer = viewLifecycleOwner.lifecycleScope.launch {
            val totalTime = 59000L
            val interval = 1000L
            var remainingTime = totalTime / 1000

            while (remainingTime > 0) {
                if (_binding != null) {
                    binding.resendTv.text = getString(R.string.resend_in, remainingTime)
                }
                delay(interval)
                remainingTime--
            }

            if (_binding != null) {
                binding.resendTv.text = getString(R.string.resend)
                binding.resendTv.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}