package com.example.fooddeliveryapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.FragmentFoodBinding
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.PopularFoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.filter.FilterDialogFragment
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.CATEGORY_REQUEST_KEY
import com.example.fooddeliveryapp.utils.DELIVERY_TIME_RANGE
import com.example.fooddeliveryapp.utils.FILTER_DIALOG
import com.example.fooddeliveryapp.utils.FILTER_REQUEST_KEY
import com.example.fooddeliveryapp.utils.MIN_RATING
import com.example.fooddeliveryapp.utils.PRICING_RANGE
import com.example.fooddeliveryapp.utils.SELECTED_CATEGORY

class FoodFragment : Fragment() {

    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var popup: PopupMenu

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupDialog()
        setupPopupMenu()
        setupObserver()
        setupFilterResultListener()
        observeFilteredRestaurants()
        observePopularFood()
    }

    private fun setupObserver() {
        setFragmentResultListener(CATEGORY_REQUEST_KEY) { _, bundle ->
            val category = bundle.getString(SELECTED_CATEGORY) ?: return@setFragmentResultListener
            if (homeViewModel.selectedFoodCategory.value != category) {
                homeViewModel.selectedFoodCategory.value = category
            }
        }

        if (homeViewModel.selectedFoodCategory.value.isNullOrEmpty()) {
            homeViewModel.selectedFoodCategory.value =
                homeViewModel.categories.value?.firstOrNull() ?: ""
        }

        homeViewModel.selectedFoodCategory.observe(viewLifecycleOwner) { category ->
            if (category.isNotEmpty()) {
                homeViewModel.filterRestaurantsByCategory(category)
                binding.foodPopupMenuBtn.text = category
                binding.popularFoodTv.text = getString(R.string.popular_with_category, category)
            }
        }
    }

    private fun setupFilterResultListener() {
        setFragmentResultListener(FILTER_REQUEST_KEY) { _, bundle ->
            val deliveryRangeArr = bundle.getIntArray(DELIVERY_TIME_RANGE)
            val deliveryTimeRange = if (deliveryRangeArr != null && deliveryRangeArr.isNotEmpty()) {
                deliveryRangeArr[0]..deliveryRangeArr.getOrElse(1) { deliveryRangeArr[0] }
            } else null

            val pricingRangeArr = bundle.getDoubleArray(PRICING_RANGE)
            val pricingRange = if (pricingRangeArr != null && pricingRangeArr.size == 2 &&
                pricingRangeArr[0] >= 0 && pricingRangeArr[1] >= 0
            ) {
                pricingRangeArr[0]..pricingRangeArr[1]
            } else null

            val minRating = bundle.getInt(MIN_RATING).takeIf { it >= 0 }

            homeViewModel.filterRestaurants(deliveryTimeRange, pricingRange, minRating)
        }
    }

    private fun observeFilteredRestaurants() {
        homeViewModel.filteredRestaurantsLiveData.observe(viewLifecycleOwner) { restaurants ->
            val restaurantModels = restaurants.map {
                RestaurantsItemModel(
                    R.drawable.ic_launcher_background,
                    it.id,
                    it.name,
                    it.description,
                    it.food.keys.joinToString(", "),
                    it.rating.toString(),
                    it.delivery,
                    it.deliveryTime
                )
            }
            openRestaurantsRecyclerAdapter.updateList(restaurantModels)
        }
    }

    private fun observePopularFood() {
        homeViewModel.popularFoodItemsLiveData.observe(viewLifecycleOwner) { foodList ->
            popularFoodRecyclerAdapter.updateList(foodList)
        }
    }

    private fun setupDialog() {
        binding.apply {
            foodBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            foodFilterIv.setOnClickListener {
                FilterDialogFragment().show(parentFragmentManager, FILTER_DIALOG)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(emptyList()) { foodItem ->
                findNavController().navigate(
                    FoodFragmentDirections.actionFoodFragmentToFoodDetailsFragment(
                        foodItem.popularFoodName,
                        foodItem.popularFoodRestaurantId
                    )
                )
            }

            openRestaurantsRecyclerAdapter =
                OpenRestaurantsRecyclerAdapter(emptyList()) { selectedRestaurant ->
                    findNavController().navigate(
                        FoodFragmentDirections.actionFoodFragmentToRestaurantViewFragment(
                            selectedRestaurant.restaurantId
                        )
                    )
                }

            recyclerPopularFood.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = popularFoodRecyclerAdapter
                isNestedScrollingEnabled = false
            }

            restaurantsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = openRestaurantsRecyclerAdapter
                isNestedScrollingEnabled = false
            }
        }
    }

    private fun setupPopupMenu() {
        binding.apply {
            popup = PopupMenu(requireContext(), foodPopupMenuBtn)

            foodPopupMenuBtn.setOnClickListener {
                popup.show()
            }

            homeViewModel.categories.observe(viewLifecycleOwner) { categories ->
                popup.menu.clear()
                categories.forEach { category ->
                    popup.menu.add(category)
                }

                popup.setOnMenuItemClickListener { item ->
                    val selected = item.title.toString()
                    homeViewModel.selectedFoodCategory.value = selected
                    true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
