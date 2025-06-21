package com.example.fooddeliveryapp.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAuthBinding
import com.example.fooddeliveryapp.utils.CODE_SENT_TO
import com.example.fooddeliveryapp.utils.CORRECT_CODE
import com.example.fooddeliveryapp.utils.ENTER_A_VALID_EMAIL
import com.example.fooddeliveryapp.utils.NEW_CODE_SENT_TO
import com.example.fooddeliveryapp.utils.SharedPreferences
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.WRONG_CODE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    private var countDownTimer: Job? = null

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
        super.onViewCreated(view, savedInstanceState)
        startingVisibility()
        setupListener()

    }

    private fun startingVisibility() {
        binding.apply {
            linearLyCodeBox.visibility = View.GONE
            resendTv.visibility = View.GONE
            authExampleEmailTv.visibility = View.GONE
        }
    }

    private fun setupListener() {
        binding.apply {
            authBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }

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

                    if (inputEmail.isNotEmpty() && inputEmail == SharedPreferences.getUserEmail(
                            requireContext()
                        )
                    ) {
                        val otp = generateOTP()
                        SharedPreferences.saveOTP(requireContext(), otp)
                        isCodeSent = true
                        showCodeInput()
                        setupPinInputListeners()
                        Toast.makeText(
                            requireContext(),
                            "$CODE_SENT_TO $inputEmail",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            ENTER_A_VALID_EMAIL,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    val inputCode = getInputOTP()
                    val savedCode = SharedPreferences.getSavedOTP(requireContext())

                    if (inputCode == savedCode) {
                        Toast.makeText(requireContext(), CORRECT_CODE, Toast.LENGTH_SHORT).show()
                        it.findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), WRONG_CODE, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            resendTv.setOnClickListener {
                startResendTimer()
                val email =
                    SharedPreferences.getUserEmail(requireContext())
                        ?: return@setOnClickListener
                val otp = generateOTP()
                SharedPreferences.saveOTP(requireContext(), otp)

                Toast.makeText(requireContext(), "$NEW_CODE_SENT_TO $email", Toast.LENGTH_SHORT)
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
        binding.apply {
            resendTv.isEnabled = false
            countDownTimer = viewLifecycleOwner.lifecycleScope.launch {
                val totalTime = 59000L
                val interval = 1000L
                var remainingTime = totalTime / 1000

                while (remainingTime > 0) {
                    if (_binding != null) {
                        resendTv.text = getString(R.string.resend_in, remainingTime)
                    }
                    delay(interval)
                    remainingTime--
                }

                if (_binding != null) {
                    resendTv.text = getString(R.string.resend)
                    resendTv.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}