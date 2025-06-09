package com.example.fooddeliveryapp.ui.fooddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentFoodDetailsBinding

class FoodDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            addToCartBtn.setOnClickListener {
                findNavController().navigate(R.id.action_foodDetailsFragment_to_homeFragment) //CHANGE AFTER ADDING API
            }
            foodDetailsBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}