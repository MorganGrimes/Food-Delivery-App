package com.example.fooddeliveryapp.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentSignupBinding
import com.example.fooddeliveryapp.utils.PLEASE_FILL
import com.example.fooddeliveryapp.utils.REGISTER_FAILED
import com.example.fooddeliveryapp.utils.REGISTRATION_COMPLETE
import com.example.fooddeliveryapp.utils.THE_PASSWORD_DO_NOT_MATCHES
import com.example.fooddeliveryapp.utils.UiUtils
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private lateinit var viewModel: SignupViewModel

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
        viewModel = SignupViewModel()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.signupResult.collect { response ->
                    response?.let {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                REGISTRATION_COMPLETE,
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                REGISTER_FAILED + { it.message() },
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
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
                        PLEASE_FILL,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                if (password != retypePassword) {
                    Toast.makeText(
                        requireContext(),
                        THE_PASSWORD_DO_NOT_MATCHES,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.register(name, email, password)
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

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

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