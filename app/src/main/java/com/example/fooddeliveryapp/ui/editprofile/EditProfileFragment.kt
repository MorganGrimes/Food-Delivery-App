package com.example.fooddeliveryapp.ui.editprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentEditProfileBinding
import com.example.fooddeliveryapp.utils.UiUtils

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupTextWatchers()
    }

    private fun setupListener() {
        binding.apply {
            saveBtn.apply {
                isEnabled = false
                setBackgroundColor(UiUtils.brownColor)
            }

            saveBtn.setOnClickListener {
                findNavController().navigate(R.id.action_editProfileFragment_to_personalProfilesFragment)
            }
            editProfileBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupTextWatchers() {
        binding.apply {
            val editTexts = listOf(
                editProfileFullNameEt,
                editProfileEmailEt,
                editProfilePhoneNumberEt,
                editProfileBioEt
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}