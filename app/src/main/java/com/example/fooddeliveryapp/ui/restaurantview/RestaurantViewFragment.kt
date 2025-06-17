package com.example.fooddeliveryapp.ui.restaurantview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            restaurantViewBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {

           /* foodRecyclerAdapter = FoodRecyclerAdapter(food) {
            }
            popularFoodRecyclerAdapter = PopularFoodRecyclerAdapter(popularFood) {
                findNavController().navigate(R.id.action_restaurantViewFragment_to_foodDetailsFragment)
            }
*/
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
