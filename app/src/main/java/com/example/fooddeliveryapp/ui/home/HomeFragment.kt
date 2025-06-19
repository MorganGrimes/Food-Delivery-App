package com.example.fooddeliveryapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.entity.AddressEntity
import com.example.fooddeliveryapp.data.model.CategoriesItemModel
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.data.remote.dto.restaurant.Restaurants
import com.example.fooddeliveryapp.databinding.FragmentHomeBinding
import com.example.fooddeliveryapp.ui.adapters.CategoriesRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.address.AddressViewModel
import com.example.fooddeliveryapp.ui.coupon.CouponDialogFragment
import com.example.fooddeliveryapp.utils.CATEGORY_REQUEST_KEY
import com.example.fooddeliveryapp.utils.COUPON_DIALOG
import com.example.fooddeliveryapp.utils.NO_ADDRESS_INSERTED
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences
import com.example.fooddeliveryapp.utils.SELECTED_CATEGORY
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter
    private val addressViewModel: AddressViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

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
        setupObservers()
        fetchData()
    }

    private fun setupObservers() {
        addressViewModel.allAddresses.observe(viewLifecycleOwner) { addresses ->
            setupHomeNameClick(addresses)
        }

        homeViewModel.categories.observe(viewLifecycleOwner) { categories ->
            val formatted = categories.map { CategoriesItemModel(R.drawable.menu, it) }
            categoriesRecyclerAdapter.updateList(formatted)
        }

        homeViewModel.filteredRestaurantsLiveData.observe(viewLifecycleOwner) { restaurants ->
            val mappedList = restaurants.map { mapRestaurantResponseToModel(it) }
            openRestaurantsRecyclerAdapter.updateList(mappedList)
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            homeViewModel.fetchCategories()
            homeViewModel.fetchRestaurants()
        }
    }

    private fun setupHomeNameClick(addresses: List<AddressEntity>) {
        binding.apply {
            val homeAddressTv = homeAddressTv
            val greetingTv = homeGreetingTv
            val name = ProfileSharedPreferences.getUserName(requireContext())

            if (addresses.isEmpty()) {
                homeAddressTv.text = NO_ADDRESS_INSERTED
                greetingTv.text = getString(R.string.greeting, name)
                homeAddressTv.setOnClickListener(null)
                return
            }

            val default = addresses.first().addressName
            homeAddressTv.text = default
            greetingTv.text = getString(R.string.greeting, default)

            homeAddressTv.setOnClickListener {
                val popup = PopupMenu(
                    ContextThemeWrapper(requireContext(), R.style.WhitePopupMenu),
                    homeAddressTv
                )

                addresses.forEachIndexed { index, address ->
                    popup.menu.add(0, index, index, address.addressName)
                }

                popup.setOnMenuItemClickListener { item ->
                    val selected = addresses[item.itemId].addressName
                    homeAddressTv.text = selected
                    greetingTv.text = getString(R.string.greeting, selected)
                    true
                }

                popup.show()
            }
        }
    }

    private fun checkAndShowCouponDialog() {
        ProfileSharedPreferences.incrementAppLaunchCount(requireContext())
        if (ProfileSharedPreferences.shouldShowCouponDialog(requireContext())) {
            CouponDialogFragment().show(parentFragmentManager, COUPON_DIALOG)
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
        categoriesRecyclerAdapter = CategoriesRecyclerAdapter(emptyList()) { category ->
            setFragmentResult(
                CATEGORY_REQUEST_KEY,
                bundleOf(SELECTED_CATEGORY to category.foodName)
            )
            findNavController().navigate(R.id.action_homeFragment_to_foodFragment)
        }

        openRestaurantsRecyclerAdapter =
            OpenRestaurantsRecyclerAdapter(emptyList()) { selectedRestaurant ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToRestaurantViewFragment(
                        selectedRestaurant.restaurantId
                    )
                )
            }

        binding.apply {
            categoriesRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = categoriesRecyclerAdapter
            }

            restaurantsRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = openRestaurantsRecyclerAdapter
                isNestedScrollingEnabled = false
            }
        }
    }

    private fun mapRestaurantResponseToModel(restaurant: Restaurants): RestaurantsItemModel {
        return RestaurantsItemModel(
            R.drawable.ic_launcher_background,
            restaurant.id,
            restaurant.name,
            restaurant.description,
            restaurant.food.keys.joinToString(", "),
            restaurant.rating.toString(),
            restaurant.delivery,
            restaurant.deliveryTime
        )
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.selectedFoodCategory.value = null
        homeViewModel.refreshRestaurants()

        val restaurants = homeViewModel.getAllRestaurants().map {
            mapRestaurantResponseToModel(it)
        }
        openRestaurantsRecyclerAdapter.updateList(restaurants)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
