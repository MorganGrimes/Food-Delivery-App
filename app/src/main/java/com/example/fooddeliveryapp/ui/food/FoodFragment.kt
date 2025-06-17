package com.example.fooddeliveryapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.FragmentFoodBinding
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.PopularFoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.filter.FilterDialogFragment
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.FILTER_DIALOG

class FoodFragment : Fragment() {

    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var popup: PopupMenu

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedCategory: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedCategory = arguments?.getString("selectedCategory") ?: ""

        setupRecyclerView()
        setupDialog()
        setupPopupMenu()
        filterByCategory(selectedCategory)
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
        popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(emptyList()) { foodItem ->
            val action = FoodFragmentDirections.actionFoodFragmentToFoodDetailsFragment(
                foodItem.popularFoodName,
                foodItem.popularFoodRestaurantId
            )
            findNavController().navigate(action)
        }

        openRestaurantsRecyclerAdapter = OpenRestaurantsRecyclerAdapter(emptyList()) {
            findNavController().navigate(R.id.action_foodFragment_to_restaurantViewFragment)
        }

        binding.recyclerPopularFood.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = popularFoodRecyclerAdapter
            isNestedScrollingEnabled = false
        }

        binding.restaurantsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = openRestaurantsRecyclerAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupPopupMenu() {
        binding.foodPopupMenuBtn.text = selectedCategory

        popup = PopupMenu(requireContext(), binding.foodPopupMenuBtn)

        binding.foodPopupMenuBtn.setOnClickListener {
            popup.show()
        }

        homeViewModel.categories.observe(viewLifecycleOwner) { categories ->
            popup.menu.clear()
            categories.forEach { category ->
                popup.menu.add(category)
            }

            popup.setOnMenuItemClickListener { item ->
                val selected = item.title.toString()
                binding.foodPopupMenuBtn.text = selected
                filterByCategory(selected)
                true
            }
        }
    }

    private fun filterByCategory(category: String) {
        val restaurants = homeViewModel.getAllRestaurants()

        val filteredFoodWithRestaurant = restaurants.flatMap { restaurant ->
            restaurant.food[category].orEmpty().map { foodItem ->
                Pair(foodItem, restaurant)
            }
        }.sortedByDescending { (foodItem, _) ->
            foodItem.eatenLastMonth
        }

        val foodModels = filteredFoodWithRestaurant.map { (foodItem, restaurant) ->
            PopularFoodItemModel(
                popularFoodImage = R.drawable.ic_launcher_background,
                popularFoodName = foodItem.name,
                popularFoodRestaurantName = restaurant.name,
                popularFoodPrice = "$${foodItem.price}",
                popularFoodRestaurantId = restaurant.id
            )
        }

        val filteredRestaurants = restaurants.filter {
            it.food.containsKey(category)
        }

        val restaurantModels = filteredRestaurants.map {
            RestaurantsItemModel(
                R.drawable.ic_launcher_background,
                it.name,
                it.food.keys.joinToString(", "),
                it.rating.toString(),
                it.delivery,
                it.deliveryTime
            )
        }

        popularFoodRecyclerAdapter.updateList(foodModels)
        openRestaurantsRecyclerAdapter.updateList(restaurantModels)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
