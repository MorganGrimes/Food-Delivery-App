package com.example.fooddeliveryapp.ui.fooddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.remote.dto.restaurant.FoodItem
import com.example.fooddeliveryapp.data.remote.dto.restaurant.Restaurants
import com.example.fooddeliveryapp.databinding.FragmentFoodDetailsBinding
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.getDrawableForCategory
import java.util.Locale

class FoodDetailsFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private var selectedSizeButton: View? = null
    private var quantity = 1

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

        setupFoodRestaurant()
        setupListener()
    }

    private fun setupFoodRestaurant() {
        val args = FoodDetailsFragmentArgs.fromBundle(requireArguments())
        val foodName = args.foodName
        val restaurantId = args.restaurantId

        val restaurant = homeViewModel.restaurants.value?.find { it.id == restaurantId }

        val foodItem = restaurant?.food?.values?.flatten()?.find { it.name == foodName }

        if (restaurant != null && foodItem != null) {
            bindFoodDetails(restaurant, foodItem)
        }
    }

    private fun bindFoodDetails(restaurant: Restaurants, foodItem: FoodItem) {
        binding.apply {

            addToCartBtn.isEnabled = false
            addToCartBtn.setBackgroundColor(UiUtils.brownColor)
            foodDetailsRestaurantsIv.setImageResource(
                getDrawableForCategory(FoodDetailsFragmentArgs.fromBundle(requireArguments()).category)
            )
            foodDetailsRestaurantNameTv.text = restaurant.name
            foodDetailsTitleTv.text = foodItem.name
            foodDetailsDescriptionTv.text = foodItem.description
            foodDetailsRatingTv.text = "${restaurant.rating}"
            foodDetailsDeliveryTv.text = restaurant.delivery
            foodDetailsDeliveryTimeTv.text = restaurant.deliveryTime

            val ingredientMap = mapOf(
                getString(R.string.salt) to saltTv,
                getString(R.string.chicken) to chickenTv,
                getString(R.string.onion) to onionTv,
                getString(R.string.garlic) to garlicTv,
                getString(R.string.pappers) to pappersTv,
                getString(R.string.ginger) to gingerTv,
                getString(R.string.broccoli) to broccoliTv,
                getString(R.string.orange) to orangeTv,
                getString(R.string.walnut) to walnutTv
            )
            ingredientMap.forEach { (ingredient, view) ->
                view.visibility =
                    if (foodItem.ingredients.contains(ingredient)) View.VISIBLE else View.GONE
            }

            foodDetailsTotalFoodPriceTv.text = getString(R.string.food_price, foodItem.price)
            foodDetailsNumberSelectedFoodTv.text =
                String.format(Locale.getDefault(), "%d", quantity)
            updateTotalPrice(foodItem.price)

            val sizeButtons =
                listOf(foodDetailsFirstSizeBtn, foodDetailsSecondSizeBtn, foodDetailsThirdSizeBtn)
            val sizes = foodItem.size

            sizeButtons.forEachIndexed { index, button ->
                if (index < sizes.size) {
                    button.visibility = View.VISIBLE
                    button.text = sizes[index]

                    button.setOnClickListener {
                        (selectedSizeButton as? TextView)?.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        selectedSizeButton?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.edit_text_white
                            )
                        )

                        button.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange
                            )
                        )
                        (button as? TextView)?.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        selectedSizeButton = button

                        addToCartBtn.isEnabled = true
                        addToCartBtn.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.orange
                            )
                        )
                    }

                } else {
                    button.visibility = View.GONE
                }
            }
        }
    }

    private fun updateTotalPrice(pricePerItem: Double) {
        val totalPrice = pricePerItem * quantity
        binding.foodDetailsTotalFoodPriceTv.text = getString(R.string.food_price, totalPrice)
    }

    private fun getCurrentFoodItemPrice(): Double {
        val args = FoodDetailsFragmentArgs.fromBundle(requireArguments())
        val restaurant = homeViewModel.restaurants.value?.find { it.id == args.restaurantId }
        val foodItem = restaurant?.food?.values?.flatten()?.find { it.name == args.foodName }
        return foodItem?.price ?: 0.0
    }

    private fun setupListener() {
        binding.apply {
            addToCartBtn.setOnClickListener {
                homeViewModel.addToCart(
                    imageRes = getDrawableForCategory(FoodDetailsFragmentArgs.fromBundle(requireArguments()).category),
                    foodName = foodDetailsTitleTv.text.toString(),
                    price = getCurrentFoodItemPrice(),
                    size = selectedSizeButton?.let { (it as TextView).text.toString() } ?: "",
                    quantity = quantity,
                    restaurantId = FoodDetailsFragmentArgs.fromBundle(requireArguments()).restaurantId
                )
                findNavController().navigate(R.id.action_foodDetailsFragment_to_myCartFragment)
            }

            foodDetailsBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }

            foodDetailsPlusIconIv.setOnClickListener {
                quantity++
                foodDetailsNumberSelectedFoodTv.text =
                    String.format(Locale.getDefault(), "%d", quantity)
                val pricePerItem = getCurrentFoodItemPrice()
                updateTotalPrice(pricePerItem)
            }

            foodDetailsMinusIconIv.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    foodDetailsNumberSelectedFoodTv.text =
                        String.format(Locale.getDefault(), "%d", quantity)
                    val pricePerItem = getCurrentFoodItemPrice()
                    updateTotalPrice(pricePerItem)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}