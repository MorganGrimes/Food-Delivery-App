package com.example.fooddeliveryapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.CategoriesItemModel
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.FragmentHomeBinding
import com.example.fooddeliveryapp.ui.adapters.CategoriesRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.coupon.CouponDialogFragment
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class HomeFragment : Fragment() {

    private lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAndShowCouponDialog()
        setupListener()
        setupRecyclerView()
    }

    private fun checkAndShowCouponDialog() {
        ProfileSharedPreferences.incrementAppLaunchCount(requireContext())
        if (ProfileSharedPreferences.shouldShowCouponDialog(requireContext())) {
            CouponDialogFragment().show(parentFragmentManager, "CouponDialog")
        }
    }

    private fun setupListener() {
        binding.apply {
            homeMenuIv.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }
            homeCartIv.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_myCartFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val categories = listOf(
                CategoriesItemModel(R.drawable.menu, getString(R.string.hot_dog)),
                CategoriesItemModel(R.drawable.menu, "Pizza"),
                CategoriesItemModel(R.drawable.menu, "Sushi")
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

            categoriesRecyclerAdapter = CategoriesRecyclerAdapter(categories) {
                findNavController().navigate(R.id.action_homeFragment_to_foodFragment)
            }
            openRestaurantsRecyclerAdapter = OpenRestaurantsRecyclerAdapter(restaurant) {
                findNavController().navigate(R.id.action_homeFragment_to_restaurantViewFragment)
            }

            categoriesRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = categoriesRecyclerAdapter
            }

            restaurantsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = openRestaurantsRecyclerAdapter
            }

            restaurantsRecycler.isNestedScrollingEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}