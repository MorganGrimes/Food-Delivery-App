package com.example.fooddeliveryapp.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentEditProfileBinding
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import com.example.fooddeliveryapp.utils.UiUtils

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        imageLauncher()
        setupListener()
        setupTextWatchers()
    }

    private fun setupListener() {
        binding.apply {
            saveBtn.setOnClickListener {
                val name = editProfileFullNameEt.text.toString()
                val email = editProfileEmailEt.text.toString()
                val phone = editProfilePhoneNumberEt.text.toString()
                val bio = editProfileBioEt.text.toString()

                ProfileSharedPreferences.saveUserProfile(requireContext(), name, email, phone, bio)

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.editProfileFragment, true)
                    .build()

                findNavController().navigate(R.id.personalProfilesFragment, null, navOptions)
            }
            editProfileBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            editProfileEditIv.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                pickImageLauncher.launch(intent)
            }
        }
    }

    private fun imageLauncher(){
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                binding.editProfileImageIv.setImageURI(imageUri)
            }
        }
    }

    private fun setupTextWatchers() {
        binding.apply {
            val editTexts = listOf(
                editProfileFullNameEt,
                editProfileEmailEt,
                editProfilePhoneNumberEt,
            )

            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val allFilled = editTexts.all { it.text?.isNotBlank() == true }
                    saveBtn.isEnabled = allFilled
                    saveBtn.setBackgroundColor(
                        if (allFilled) requireContext().getColor(R.color.orange) else UiUtils.brownColor
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

            editTexts.forEach { it.addTextChangedListener(watcher) }
        }
    }

    private fun loadUserData() {
        val context = requireContext()
        binding.apply {
            editProfileFullNameEt.setText(ProfileSharedPreferences.getUserName(context))
            editProfileEmailEt.setText(ProfileSharedPreferences.getUserEmail(context))
            editProfilePhoneNumberEt.setText(ProfileSharedPreferences.getUserPhone(context))
            editProfileBioEt.setText(ProfileSharedPreferences.getUserBio(context))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}