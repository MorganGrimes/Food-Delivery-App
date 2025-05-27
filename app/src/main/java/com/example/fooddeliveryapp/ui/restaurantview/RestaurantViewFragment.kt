package com.example.fooddeliveryapp.ui.restaurantview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.FoodItemModel
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.databinding.FragmentRestaurantViewBinding
import com.example.fooddeliveryapp.ui.adapters.FoodRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.PopularFoodRecyclerAdapter

class RestaurantViewFragment : Fragment() {

    private lateinit var foodRecyclerAdapter: FoodRecyclerAdapter
    private lateinit var popularFoodRecyclerAdapter: PopularFoodRecyclerAdapter

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
        setupRecyclerView()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupRecyclerView() {
        binding.apply {
            val food = listOf(
                FoodItemModel(
                    getString(R.string.burger)
                ), FoodItemModel(
                    getString(R.string.burger)
                ), FoodItemModel(
                    getString(R.string.burger)
                ), FoodItemModel(
                    getString(R.string.burger)
                )
            )

            val popularFood = listOf(
                PopularFoodItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.burger_bistro),
                    getString(R.string.rose_garden),
                    getString(R.string._40)
                ), PopularFoodItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.burger_bistro),
                    getString(R.string.rose_garden),
                    getString(R.string._40)
                ), PopularFoodItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.burger_bistro),
                    getString(R.string.rose_garden),
                    getString(R.string._40)
                )
            )

            foodRecyclerAdapter = FoodRecyclerAdapter(food) {
            }
            popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(popularFood) {
            }

            recyclerFood.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = foodRecyclerAdapter
            }

            recyclerRestaurantFoodSelected.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = popularFoodRecyclerAdapter
            }
        }
    }
}
