package com.example.fooddeliveryapp.ui.addnewaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.address.AddressEntity
import com.example.fooddeliveryapp.databinding.FragmentAddNewAddressBinding
import com.example.fooddeliveryapp.ui.address.AddressViewModel
import com.example.fooddeliveryapp.utils.ADDRESS_ID
import com.example.fooddeliveryapp.utils.FILL_FIELDS
import com.example.fooddeliveryapp.utils.HOME
import com.example.fooddeliveryapp.utils.OTHER
import com.example.fooddeliveryapp.utils.SELECT_LABEL
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.WORK

class AddNewAddressFragment : Fragment() {

    private var _binding: FragmentAddNewAddressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddressViewModel by activityViewModels()
    private var currentAddressId: Int = -1
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
        setupAddresses()
        setupListener()
    }

    private fun setupAddresses() {
        val addressId = arguments?.getInt(ADDRESS_ID, -1) ?: -1
        currentAddressId = addressId
        if (currentAddressId != -1) {
            viewModel.allAddresses.observe(viewLifecycleOwner) { addresses ->
                val address = addresses.find { it.id == currentAddressId }
                address?.let { populateFields(it) }
            }
        }
    }

    private fun populateFields(address: AddressEntity) {
        binding.apply {
            addNewAddressAddressEt.setText(address.addressName)
            addNewAddressStreetEt.setText(address.addressStreet)
            addNewAddressPostCodeEt.setText(address.addressPostCode)
            addNewAddressApartmentEt.setText(address.addressApartment)
            selectLabelButton(address.addressLabel)
        }
    }

    private fun setupListener() {
        binding.apply {
            val inputs = listOf(
                addNewAddressAddressEt,
                addNewAddressStreetEt,
                addNewAddressPostCodeEt,
                addNewAddressApartmentEt
            )

            inputs.forEach { it.addTextChangedListener { checkFormValidity() } }

            addNewAddressHomeBtn.setOnClickListener { selectLabelButton(HOME) }
            addNewAddressWorkBtn.setOnClickListener { selectLabelButton(WORK) }
            addNewAddressOtherBtn.setOnClickListener { selectLabelButton(OTHER) }

            saveLocationBtn.isEnabled = false
            saveLocationBtn.setBackgroundColor(UiUtils.brownColor)

            saveLocationBtn.setOnClickListener {
                val label = selectedLabel
                if (label.isNullOrBlank()) {
                    Toast.makeText(
                        requireContext(),
                        SELECT_LABEL,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val name = binding.addNewAddressAddressEt.text.toString().trim()
                val street = binding.addNewAddressStreetEt.text.toString().trim()
                val postCode = binding.addNewAddressPostCodeEt.text.toString().trim()
                val apartment = binding.addNewAddressApartmentEt.text.toString().trim()

                if (name.isEmpty() || street.isEmpty() || postCode.isEmpty() || apartment.isEmpty()) {
                    Toast.makeText(requireContext(), FILL_FIELDS, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val address = AddressEntity(
                    id = if (currentAddressId != -1) currentAddressId else 0,
                    addressLabel = label,
                    addressName = name,
                    addressStreet = street,
                    addressPostCode = postCode,
                    addressApartment = apartment
                )

                if (currentAddressId != -1) {
                    viewModel.update(address)
                } else {
                    viewModel.insert(address)
                }

                findNavController().navigate(R.id.action_addNewAddressFragment_to_addressFragment)
            }
        }
    }

    private fun selectLabelButton(label: String) {
        binding.apply {
            selectedLabel = label
            val defaultBg = resources.getColor(R.color.edit_text_white, null)
            val selectedBg = resources.getColor(R.color.orange, null)
            val defaultText = resources.getColor(R.color.black, null)
            val selectedText = resources.getColor(R.color.white, null)

            addNewAddressHomeBtn.setBackgroundColor(if (label == HOME) selectedBg else defaultBg)
            addNewAddressHomeBtn.setTextColor(if (label == HOME) selectedText else defaultText)

            addNewAddressWorkBtn.setBackgroundColor(if (label == WORK) selectedBg else defaultBg)
            addNewAddressWorkBtn.setTextColor(if (label == WORK) selectedText else defaultText)

            addNewAddressOtherBtn.setBackgroundColor(if (label == OTHER) selectedBg else defaultBg)
            addNewAddressOtherBtn.setTextColor(if (label == OTHER) selectedText else defaultText)

            checkFormValidity()
        }
    }

    private fun checkFormValidity() {
        binding.apply {
            val valid = addNewAddressAddressEt.text?.isNotBlank() == true &&
                    addNewAddressStreetEt.text?.isNotBlank() == true &&
                    addNewAddressPostCodeEt.text?.isNotBlank() == true &&
                    addNewAddressApartmentEt.text?.isNotBlank() == true &&
                    selectedLabel != null

            saveLocationBtn.isEnabled = valid
            saveLocationBtn.setBackgroundColor(
                if (valid) resources.getColor(R.color.orange, null) else UiUtils.brownColor
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
