package com.example.fooddeliveryapp.ui.restaurantview

import android.os.Bundle
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
import com.example.fooddeliveryapp.utils.getDrawableForCategory
import java.util.Locale

class RestaurantViewFragment : Fragment() {

    private lateinit var foodRecyclerAdapter: FoodRecyclerAdapter
    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val args: RestaurantViewFragmentArgs by navArgs()
    private var selectedCategory: String = ""
    private var currentRestaurant: Restaurants? = null

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

        selectedCategory = savedInstanceState?.getString("selectedCategory") ?: ""

        val restaurantId = args.restaurantId
        currentRestaurant = homeViewModel.getAllRestaurants().firstOrNull { it.id == restaurantId }

        if (homeViewModel.selectedRestaurantId != restaurantId) {
            homeViewModel.selectedRestaurantId = restaurantId
            selectedCategory = ""
            homeViewModel.selectedCategory.value = ""
        } else {
            selectedCategory = homeViewModel.selectedCategory.value ?: ""
        }

        setupRestaurantView()
        setupListener()
        setupCategoryRecycler()
        setupFoodRecycler()
        updateInitialCategoryAndFood()
    }

    private fun updateInitialCategoryAndFood() {
        val categories = currentRestaurant?.food?.keys?.toList() ?: emptyList()

        if (selectedCategory.isEmpty() && categories.isNotEmpty()) {
            selectedCategory = categories.first()
            homeViewModel.selectedCategory.value = selectedCategory
        }

        if (selectedCategory.isNotEmpty()) {
            currentRestaurant?.let {
                updatePopularFoodList(it, selectedCategory)
                binding.restaurantViewFoodNameAndNumberTv.text = getString(
                    R.string.category_food_count,
                    selectedCategory,
                    it.food[selectedCategory]?.size ?: 0
                )
            }
        }
    }

    private fun setupRestaurantView() {
        binding.apply {
        currentRestaurant?.let { restaurant ->
            restaurantViewRestaurantNameTv.text = restaurant.name
            restaurantViewRestaurantDescriptionTv.text = restaurant.description
            restaurantsIv.setImageResource(R.drawable.restaurant)
            restaurantViewRatingTv.text =
                String.format(Locale.getDefault(), "%.1f", restaurant.rating)
            restaurantViewDeliveryTv.text = restaurant.delivery
            restaurantViewDeliveryTimeTv.text = restaurant.deliveryTime
        }
        }
    }

    private fun setupListener() {
        binding.restaurantViewBackIconIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupCategoryRecycler() {
        val categories = currentRestaurant?.food?.keys?.map { FoodItemModel(it) } ?: emptyList()

        foodRecyclerAdapter = FoodRecyclerAdapter(categories) { item ->
            selectedCategory = item.foodName
            homeViewModel.selectedCategory.value = selectedCategory

            currentRestaurant?.let {
                updatePopularFoodList(it, selectedCategory)
                binding.restaurantViewFoodNameAndNumberTv.text = getString(
                    R.string.category_food_count,
                    selectedCategory,
                    it.food[selectedCategory]?.size ?: 0
                )
            }
        }

        binding.recyclerRestaurantCategoryFood.apply {
            adapter = foodRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupFoodRecycler() {
        popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(emptyList()) { selectedFood ->
            findNavController().navigate(
                RestaurantViewFragmentDirections.actionRestaurantViewFragmentToFoodDetailsFragment(
                    selectedFood.popularFoodName,
                    selectedFood.popularFoodRestaurantId
                )
            )
        }

        binding.recyclerRestaurantFoodSelected.apply {
            adapter = popularFoodRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updatePopularFoodList(restaurant: Restaurants, category: String) {
        val foodItems = restaurant.food[category]?.map { foodItem ->
            PopularFoodItemModel(
                popularFoodImage = getDrawableForCategory(category),
                popularFoodName = foodItem.name,
                popularFoodRestaurantName = restaurant.name,
                popularFoodPrice = "$${foodItem.price}",
                popularFoodRestaurantId = restaurant.id
            )
        } ?: emptyList()

        popularFoodRecyclerAdapter.updateList(foodItems)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedCategory", selectedCategory)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
