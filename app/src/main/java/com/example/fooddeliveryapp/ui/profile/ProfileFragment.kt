package com.example.fooddeliveryapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentProfileBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserProfile()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            profileBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            profilePersonalInfoLl.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_personalProfilesFragment)
            }
            profileAddressLl.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addressFragment)
            }
            profileCartLl.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_myCartFragment)
            }
            profilePaymentMethodLl.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_paymentFragment)
            }
            profileMyOrdersLl.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_myOrdersTabsFragment)
            }
        }
    }

    private fun loadUserProfile() {
        val context = requireContext()
        binding.apply {
            val name = ProfileSharedPreferences.getUserName(context)
            val bio = ProfileSharedPreferences.getUserBio(context)

            profileNameTv.text = name ?: ""
            profileBioTv.text = bio ?: ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}