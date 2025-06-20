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
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.databinding.FragmentMyCartBinding
import com.example.fooddeliveryapp.ui.adapters.CartItemRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.UiUtils
import java.util.Locale

class MyCartFragment : Fragment() {

    private lateinit var cartItemRecyclerAdapter: CartItemRecyclerAdapter
    private val viewModel: HomeViewModel by activityViewModels()

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
            cartItemRecyclerAdapter.updateData(cartItems)
            updateCartTotal(cartItems)
        }
    }

    private fun setupListener() {
        binding.apply {
            myCartBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            placeOrderBtn.setOnClickListener {
                val bundle = Bundle().apply {
                    putBoolean("showBottomBar", true)
                }
                findNavController().navigate(R.id.action_myCartFragment_to_paymentFragment, bundle)
            }
            myCartEditAddressTv.setOnClickListener {
                findNavController().navigate(R.id.action_myCartFragment_to_addressFragment)
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

    private fun updateCartTotal(cartItems: List<CartItemModel>) {
        binding.apply {
            val total = cartItems.sumOf { it.cartFoodPrice }
            myCartCartTotalPriceTv.text = String.format(Locale.getDefault(), "$%.2f", total)
            viewModel.updateCartTotalPrice()

            if (total == 0.0) {
                placeOrderBtn.isEnabled = false
                placeOrderBtn.setBackgroundColor(UiUtils.brownColor)
            } else {
                placeOrderBtn.isEnabled = true
                placeOrderBtn.setBackgroundColor(requireContext().getColor(R.color.orange))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}