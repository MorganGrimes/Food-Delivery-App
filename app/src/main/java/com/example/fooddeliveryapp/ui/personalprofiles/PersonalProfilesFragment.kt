package com.example.fooddeliveryapp.ui.personalprofiles

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentPersonalProfilesBinding
import com.example.fooddeliveryapp.utils.SharedPreferences

class PersonalProfilesFragment : Fragment() {

    private var _binding: FragmentPersonalProfilesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalProfilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserProfile()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            personalProfilesEditTv.setOnClickListener {
                findNavController().navigate(R.id.action_personalProfilesFragment_to_editProfileFragment)
            }
            personalProfilesBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun loadUserProfile() {
        val context = requireContext()
        binding.apply {
            val name = SharedPreferences.getUserName(context)
            val email = SharedPreferences.getUserEmail(context)
            val phone = SharedPreferences.getUserPhone(context)
            val bio = SharedPreferences.getUserBio(context)
            val image = SharedPreferences.getProfileImage(context)

            personalProfilesNameTv.text = name ?: ""
            personalProfilesProfileNameTv.text = name ?: ""
            personalProfilesProfileEmailTv.text = email ?: ""
            personalProfilesProfilePhoneNumberTv.text = phone ?: ""
            personalProfilesBioTv.text = bio ?: ""
            if (!image.isNullOrEmpty()) {
                val imageBytes = Base64.decode(image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                personalProfilesImageIv.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}