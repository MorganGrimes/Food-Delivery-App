package com.example.fooddeliveryapp.ui.restaurantview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.FoodItemModel
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.data.remote.dto.restaurant.Restaurants
import com.example.fooddeliveryapp.databinding.FragmentRestaurantViewBinding
import com.example.fooddeliveryapp.ui.adapters.FoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.PopularFoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import java.util.Locale

class RestaurantViewFragment : Fragment() {

    private lateinit var foodRecyclerAdapter: FoodRecyclerAdapter
    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val args: RestaurantViewFragmentArgs by navArgs()
    private var selectedCategory: String = ""

    private var _binding: FragmentRestaurantViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestaurantViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRestaurantView()
        setupRecyclerView()
        setupListener()
    }

    private fun setupRestaurantView() {
        val restaurantId = args.restaurantId
        val restaurant = homeViewModel.getAllRestaurants().firstOrNull { it.id == restaurantId }

        binding.apply {
            restaurant?.let {
                restaurantViewRestaurantNameTv.text = it.name
                restaurantViewRestaurantDescriptionTv.text = it.description
                restaurantViewRatingTv.text = String.format(Locale.getDefault(), "%.1f", it.rating)
                restaurantViewDeliveryTv.text = it.delivery
                restaurantViewDeliveryTimeTv.text = it.deliveryTime
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            restaurantViewBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        val restaurantId = args.restaurantId
        val restaurant = homeViewModel.getAllRestaurants().firstOrNull { it.id == restaurantId }

        val categories = restaurant?.food?.keys?.map { categoryName ->
            FoodItemModel(categoryName)
        } ?: emptyList()

        foodRecyclerAdapter = FoodRecyclerAdapter(categories) { selectedCategory ->
            val newlySelectedCategory = selectedCategory.foodName
            Log.d("RestaurantView", "Category clicked: $newlySelectedCategory")

            this.selectedCategory = newlySelectedCategory
            updatePopularFoodList(restaurant, newlySelectedCategory)
            binding.restaurantViewFoodNameAndNumberTv.text = getString(
                R.string.category_food_count,
                newlySelectedCategory,
                restaurant?.food?.get(newlySelectedCategory)?.size ?: 0
            )
        }

        binding.recyclerRestaurantCategoryFood.apply {
            adapter = foodRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        if (categories.isNotEmpty()) {
            selectedCategory = categories[0].foodName
            updatePopularFoodList(restaurant, selectedCategory)
            binding.restaurantViewFoodNameAndNumberTv.text = getString(
                R.string.category_food_count,
                selectedCategory,
                restaurant?.food?.get(selectedCategory)?.size ?: 0
            )
        }
    }

    private fun updatePopularFoodList(restaurant: Restaurants?, category: String) {
        val foodItems = restaurant?.food?.get(category)?.map { foodItem ->
            PopularFoodItemModel(
                popularFoodImage = R.drawable.ic_launcher_background,
                popularFoodName = foodItem.name,
                popularFoodRestaurantName = restaurant.name,
                popularFoodPrice = "$${foodItem.price}",
                popularFoodRestaurantId = restaurant.id
            )
        } ?: emptyList()
        if (::popularFoodRecyclerAdapter.isInitialized) {
            popularFoodRecyclerAdapter.updateList(foodItems)
        } else {
            popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(foodItems) { selectedFood ->

            }
            binding.recyclerRestaurantFoodSelected.apply {
                adapter = popularFoodRecyclerAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
