package com.example.fooddeliveryapp.ui.mycart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentMyCartBinding
import com.example.fooddeliveryapp.ui.adapters.CartItemRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel

class MyCartFragment : Fragment() {

    private lateinit var cartItemRecyclerAdapter: CartItemRecyclerAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var isEditMode: Boolean = false

    private var _binding: FragmentMyCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListener()
        setupRecyclerView()

        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItemRecyclerAdapter.updateData(cartItems, isEditMode)
        }
    }

    private fun setupListener() {
        binding.apply {
            myCartBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            placeOrderBtn.setOnClickListener {
                findNavController().navigate(R.id.action_myCartFragment_to_paymentFragment)
            }
            myCartEditAddressTv.setOnClickListener {
                findNavController().navigate(R.id.action_myCartFragment_to_addressFragment)
            }
            myCartEditItemsTv.setOnClickListener {
                isEditMode = !isEditMode
                cartItemRecyclerAdapter.setEditMode(isEditMode)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerCartItem.apply {
                layoutManager = LinearLayoutManager(requireContext())
                cartItemRecyclerAdapter = CartItemRecyclerAdapter(viewModel, emptyList())
                adapter = cartItemRecyclerAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}