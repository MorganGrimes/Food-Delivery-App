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
import java.text.SimpleDateFormat
import java.util.Date
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

        viewModel.cartItems.observe(viewLifecycleOwner) { allCartItems ->
            val currentItems = allCartItems.filter { it.cartId == viewModel.cartId.value }
            cartItemRecyclerAdapter.updateData(currentItems)
            updateCartTotal(currentItems)
            applyCouponIfAvailable(currentItems)
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

            cartCouponRemoveIv.setOnClickListener {
                cartCouponCode.text = ""

                val currentItems = viewModel.cartItems.value?.filter {
                    it.cartId == viewModel.cartId.value
                } ?: emptyList()

                val total = currentItems.sumOf { it.cartFoodPrice }
                myCartCartTotalPriceTv.text =
                    String.format(Locale.getDefault(), "$%.2f", total)
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

    private fun applyCouponIfAvailable(cartItems: List<CartItemModel>) {
        binding.apply {
            val now = Date()

            val validCoupon = viewModel.coupons.value?.firstOrNull { coupon ->
                val start =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(
                        coupon.startDate
                    )
                val end =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(
                        coupon.endDate
                    )
                val isInDateRange =
                    start != null && end != null && now.after(start) && now.before(end)

                val selectedCategory = viewModel.selectedFoodCategory.value
                val hasProductInCart = selectedCategory != null &&
                        cartItems.any { cartItem ->
                            coupon.products.any { productName ->
                                productName.equals(cartItem.cartFoodName, ignoreCase = true)
                            }
                        }
                isInDateRange && hasProductInCart
            }

            if (validCoupon != null) {
                cartCouponCode.text = validCoupon.code
                cartCouponRemoveIv.visibility = View.VISIBLE

                val total = cartItems.sumOf { it.cartFoodPrice }
                val discount = total * validCoupon.discountPercentage / 100
                val discountedTotal = total - discount

                myCartCartTotalPriceTv.text =
                    String.format(Locale.getDefault(), "$%.2f", discountedTotal)
            } else {
                cartCouponCode.text = ""
                cartCouponRemoveIv.visibility = View.GONE
                val total = cartItems.sumOf { it.cartFoodPrice }
                myCartCartTotalPriceTv.text = String.format(Locale.getDefault(), "$%.2f", total)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}