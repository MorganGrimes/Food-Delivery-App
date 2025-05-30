package com.example.fooddeliveryapp.ui.mycart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.databinding.FragmentMyCartBinding
import com.example.fooddeliveryapp.ui.adapters.CartItemRecyclerAdapter

class MyCartFragment : Fragment() {

    private lateinit var cartItemRecyclerAdapter: CartItemRecyclerAdapter

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
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val cart = listOf(
                CartItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.pizza_calzone_european),
                    getString(R.string._64),
                    getString(R.string._14)
                ),
                CartItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.pizza_calzone_european),
                    getString(R.string._64),
                    getString(R.string._14)
                ),
                CartItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.pizza_calzone_european),
                    getString(R.string._64),
                    getString(R.string._14)
                ),
                CartItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.pizza_calzone_european),
                    getString(R.string._64),
                    getString(R.string._14)
                ),
                CartItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.pizza_calzone_european),
                    getString(R.string._64),
                    getString(R.string._14)
                )
            )

            cartItemRecyclerAdapter = CartItemRecyclerAdapter(cart)

            recyclerCartItem.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = cartItemRecyclerAdapter
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}