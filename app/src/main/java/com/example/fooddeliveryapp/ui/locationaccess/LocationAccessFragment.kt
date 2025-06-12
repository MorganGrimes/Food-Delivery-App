package com.example.fooddeliveryapp.ui.locationaccess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fooddeliveryapp.databinding.FragmentLocationAccessBinding

class LocationAccessFragment : Fragment() {

    private var _binding: FragmentLocationAccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationAccessBinding.inflate(inflater, container, false)
        return binding.root
    }
}