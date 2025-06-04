package com.example.fooddeliveryapp.ui.home

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.entity.AddressEntity
import com.example.fooddeliveryapp.data.model.CategoriesItemModel
import com.example.fooddeliveryapp.data.model.RestaurantsItemModel
import com.example.fooddeliveryapp.databinding.FragmentHomeBinding
import com.example.fooddeliveryapp.ui.adapters.CategoriesRecyclerAdapter
import com.example.fooddeliveryapp.ui.adapters.OpenRestaurantsRecyclerAdapter
import com.example.fooddeliveryapp.ui.address.AddressViewModel
import com.example.fooddeliveryapp.ui.coupon.CouponDialogFragment
import com.example.fooddeliveryapp.utils.COUPON_DIALOG
import com.example.fooddeliveryapp.utils.NO_ADDRESS_INSERTED
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class HomeFragment : Fragment() {

    private lateinit var categoriesRecyclerAdapter: CategoriesRecyclerAdapter
    private lateinit var openRestaurantsRecyclerAdapter: OpenRestaurantsRecyclerAdapter
    private val addressViewModel: AddressViewModel by activityViewModels()

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
        addressViewModel.allAddresses.observe(viewLifecycleOwner) { addresses ->
            setupHomeNameClick(addresses)
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
        binding.apply {
            val categories = listOf(
                CategoriesItemModel(R.drawable.menu, getString(R.string.hot_dog)),
                CategoriesItemModel(R.drawable.menu, getString(R.string.pizza)),
                CategoriesItemModel(R.drawable.menu, getString(R.string.sushi))
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