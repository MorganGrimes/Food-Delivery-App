package com.example.fooddeliveryapp.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.FragmentFoodBinding
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.PopularFoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.filter.FilterDialogFragment

class FoodFragment : Fragment() {

    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter

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

    }

    private fun setupDialog() {
        binding.apply {
            foodBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            foodFilterIv.setOnClickListener {
                FilterDialogFragment().show(parentFragmentManager, "FilterDialog")
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val popularFood = listOf(
                PopularFoodItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.burger_bistro),
                    getString(R.string.rose_garden),
                    getString(R.string._40)
                ),
                PopularFoodItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.smokin_burger),
                    getString(R.string.cafenio_restaurant),
                    getString(R.string._60)
                )
            )

            val restaurant = listOf(
                RestaurantsItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.rose_garden),
                    getString(R.string.food_example),
                    getString(R.string.rating),
                    getString(R.string.free),
                    getString(R.string._20_min)
                ), RestaurantsItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.rose_garden),
                    getString(R.string.food_example),
                    getString(R.string.rating),
                    getString(R.string.free),
                    getString(R.string._20_min)
                ), RestaurantsItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.rose_garden),
                    getString(R.string.food_example),
                    getString(R.string.rating),
                    getString(R.string.free),
                    getString(R.string._20_min)
                ), RestaurantsItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.rose_garden),
                    getString(R.string.food_example),
                    getString(R.string.rating),
                    getString(R.string.free),
                    getString(R.string._20_min)
                )
            )

            popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(popularFood) {
                findNavController().navigate(R.id.action_foodFragment_to_foodDetailsFragment)
            }

            openRestaurantsRecyclerAdapter = OpenRestaurantsRecyclerAdapter(restaurant) {
                findNavController().navigate(R.id.action_foodFragment_to_restaurantViewFragment)
            }

            recyclerPopularFood.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = popularFoodRecyclerAdapter
            }

            restaurantsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = openRestaurantsRecyclerAdapter
            }

            recyclerPopularFood.isNestedScrollingEnabled = false
            restaurantsRecycler.isNestedScrollingEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}