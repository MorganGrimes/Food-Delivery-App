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
            updateCartTotal(cartItems)
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

                if (isEditMode) {
                    myCartEditItemsTv.text = getString(R.string.done)
                    myCartEditItemsTv.setTextColor(requireContext().getColor(R.color.green))
                } else {
                    myCartEditItemsTv.text = getString(R.string.edit_items)
                    myCartEditItemsTv.setTextColor(requireContext().getColor(R.color.red))
                }
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
        val total = cartItems.sumOf { it.cartFoodPrice }
        binding.myCartCartTotalPriceTv.text = String.format(Locale.getDefault(), "$%.2f", total)

        if (total == 0.0) {
            binding.placeOrderBtn.isEnabled = false
            binding.placeOrderBtn.setBackgroundColor(UiUtils.brownColor)
        } else {
            binding.placeOrderBtn.isEnabled = true
            binding.placeOrderBtn.setBackgroundColor(requireContext().getColor(R.color.orange))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}