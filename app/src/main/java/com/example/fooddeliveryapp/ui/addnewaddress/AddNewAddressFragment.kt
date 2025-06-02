package com.example.fooddeliveryapp.ui.addnewaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAddNewAddressBinding
import com.example.fooddeliveryapp.utils.UiUtils

class AddNewAddressFragment : Fragment() {

    private var _binding: FragmentAddNewAddressBinding? = null
    private val binding get() = _binding!!

    private var selectedLabel: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            val inputs = listOf(
                addNewAddressAddressEt,
                addNewAddressStreetEt,
                addNewAddressPostCodeEt,
                addNewAddressApartmentEt
            )

            inputs.forEach { editText ->
                editText.addTextChangedListener {
                    checkFormValidity()
                }
            }

            addNewAddressHomeBtn.setOnClickListener {
                selectLabelButton("home")
            }

            addNewAddressWorkBtn.setOnClickListener {
                selectLabelButton("work")
            }

            addNewAddressOtherBtn.setOnClickListener {
                selectLabelButton("other")
            }

            saveLocationBtn.isEnabled = false
            saveLocationBtn.setBackgroundColor(UiUtils.brownColor)
            saveLocationBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addNewAddressFragment_to_addressFragment)
            }

            addNewAddressBackIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun selectLabelButton(label: String) {
        binding.apply {
            selectedLabel = label

            val defaultBgColor = resources.getColor(R.color.edit_text_white, null)
            val selectedBgColor = resources.getColor(R.color.orange, null)
            val defaultTextColor = resources.getColor(R.color.black, null)
            val selectedTextColor = resources.getColor(R.color.white, null)

            addNewAddressHomeBtn.setBackgroundColor(if (label == "home") selectedBgColor else defaultBgColor)
            addNewAddressHomeBtn.setTextColor(if (label == "home") selectedTextColor else defaultTextColor)

            addNewAddressWorkBtn.setBackgroundColor(if (label == "work") selectedBgColor else defaultBgColor)
            addNewAddressWorkBtn.setTextColor(if (label == "work") selectedTextColor else defaultTextColor)

            addNewAddressOtherBtn.setBackgroundColor(if (label == "other") selectedBgColor else defaultBgColor)
            addNewAddressOtherBtn.setTextColor(if (label == "other") selectedTextColor else defaultTextColor)

            checkFormValidity()
        }
    }

    private fun checkFormValidity() {
        binding.apply {
            val isAddressFilled = addNewAddressAddressEt.text?.isNotBlank() == true
            val isStreetFilled = addNewAddressStreetEt.text?.isNotBlank() == true
            val isPostCodeFilled = addNewAddressPostCodeEt.text?.isNotBlank() == true
            val isApartmentFilled = addNewAddressApartmentEt.text?.isNotBlank() == true
            val isLabelSelected = selectedLabel != null

            val isFormValid =
                isAddressFilled && isStreetFilled && isPostCodeFilled && isApartmentFilled && isLabelSelected

            saveLocationBtn.isEnabled = isFormValid
            saveLocationBtn.setBackgroundColor(
                if (isFormValid) resources.getColor(R.color.orange, null)
                else UiUtils.brownColor
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}