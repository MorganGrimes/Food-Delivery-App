package com.example.fooddeliveryapp.ui.addcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAddCardBinding
import com.example.fooddeliveryapp.utils.UiUtils

class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            addEMakePaymentBtn.apply {
                isEnabled = false
                setBackgroundColor(UiUtils.brownColor)
            }
            addCardBackIv.setOnClickListener {
                findNavController().popBackStack()
            }
            addEMakePaymentBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addCardFragment_to_paymentFragment)
            }


            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    validateInputs()
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

            addCardCardHolderNameEt.addTextChangedListener(textWatcher)
            addCardCardNumberEt.addTextChangedListener(textWatcher)
            addCardExpireDateCardEt.addTextChangedListener(textWatcher)
            addCardCvcEt.addTextChangedListener(textWatcher)

            addCardExpireDateCardEt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.length == 2 && !s.contains("/")) {
                        s.insert(2, "/")
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun validateInputs() {
        binding.apply {
            val name = addCardCardHolderNameEt.text.toString().trim()
            val number = addCardCardNumberEt.text.toString().trim()
            val expire = addCardExpireDateCardEt.text.toString().trim()
            val cvc = addCardCvcEt.text.toString().trim()

            val isNameValid = name.isNotEmpty()
            val isNumberValid = number.length >= 13
            val isCvcValid = cvc.matches(Regex("^\\d{3}$"))
            val isExpireValid = expire.matches(Regex("^(0[1-9]|1[0-2])/\\d{4}$"))

            val isFormValid = isNameValid && isNumberValid && isCvcValid && isExpireValid

            addEMakePaymentBtn.apply {
                isEnabled = isFormValid
                setBackgroundColor(if (isFormValid) requireContext().getColor(R.color.orange) else UiUtils.brownColor)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}