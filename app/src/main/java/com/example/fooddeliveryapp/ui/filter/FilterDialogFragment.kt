package com.example.fooddeliveryapp.ui.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentFilterDialogBinding
import com.example.fooddeliveryapp.utils.onBackPressed
import com.example.fooddeliveryapp.utils.setupDialogMargins

class FilterDialogFragment : DialogFragment() {

    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        onBackPressed()
        setupDialogMargins(view)

    }

    private fun setupListener() {
        binding.filterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_filterDialogFragment_to_foodFragment)
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