package com.example.fooddeliveryapp.ui.addressdelete

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.fooddeliveryapp.databinding.FragmentAddressDeleteDialogBinding
import com.example.fooddeliveryapp.utils.setupDialogMargins

class AddressDeleteDialogFragment(
    private val onConfirmDelete: () -> Unit
) : DialogFragment() {

    private var _binding: FragmentAddressDeleteDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogMargins(view)
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            yesBtn.setOnClickListener {
                onConfirmDelete()
                dismiss()
            }

            noBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.1F)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}