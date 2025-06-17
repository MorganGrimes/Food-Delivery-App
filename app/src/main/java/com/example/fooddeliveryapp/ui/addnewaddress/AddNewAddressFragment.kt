package com.example.fooddeliveryapp.ui.addnewaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.entity.AddressEntity
import com.example.fooddeliveryapp.databinding.FragmentAddNewAddressBinding
import com.example.fooddeliveryapp.ui.address.AddressViewModel
import com.example.fooddeliveryapp.utils.ADDRESS_ID
import com.example.fooddeliveryapp.utils.FILL_FIELDS
import com.example.fooddeliveryapp.utils.HOME
import com.example.fooddeliveryapp.utils.INITIAL_POSITION
import com.example.fooddeliveryapp.utils.OTHER
import com.example.fooddeliveryapp.utils.SELECT_LABEL
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.WORK
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class AddNewAddressFragment : Fragment() {

    private val viewModel: AddressViewModel by activityViewModels()
    private var currentAddressId: Int = -1
    private var selectedLabel: String? = null

    private var _binding: FragmentAddNewAddressBinding? = null
    private val binding get() = _binding!!

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
        setupMap()
    }

    private fun setupMap() {
        Configuration.getInstance().load(requireContext(),
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(requireContext()))

        val map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val startPoint = GeoPoint(41.9028, 12.4964)
        map.controller.setZoom(15.0)
        map.controller.setCenter(startPoint)

        val marker = Marker(map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = INITIAL_POSITION
        map.overlays.add(marker)
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

            addNewAddressBackIv.setOnClickListener {
                findNavController().popBackStack()
            }
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

                val name = addNewAddressAddressEt.text.toString().trim()
                val street = addNewAddressStreetEt.text.toString().trim()
                val postCode = addNewAddressPostCodeEt.text.toString().trim()
                val apartment = addNewAddressApartmentEt.text.toString().trim()

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

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.addNewAddressFragment, true)
                    .build()

                findNavController().navigate(R.id.addressFragment, null, navOptions)
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

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
