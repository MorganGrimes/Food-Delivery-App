package com.example.fooddeliveryapp.ui.myorderstabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fooddeliveryapp.databinding.FragmentMyOrdersTabsBinding
import com.example.fooddeliveryapp.ui.adapters.MyPagerAdapter
import com.example.fooddeliveryapp.ui.history.HistoryFragment
import com.example.fooddeliveryapp.ui.ongoing.OngoingFragment
import com.example.fooddeliveryapp.utils.HISTORY
import com.example.fooddeliveryapp.utils.ONGOING
import com.google.android.material.tabs.TabLayoutMediator

class MyOrdersTabsFragment : Fragment() {

    private lateinit var myAdapter: MyPagerAdapter
    private var tabsArray = arrayOf(ONGOING, HISTORY)

    private var _binding: FragmentMyOrdersTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersTabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        setupListeners()
    }

    private fun initTabLayout() {
        binding.apply {
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            myAdapter = MyPagerAdapter(childFragmentManager, lifecycle)
            myAdapter.addFragmentToList(OngoingFragment())
            myAdapter.addFragmentToList(HistoryFragment())

            viewPager.adapter = myAdapter

            TabLayoutMediator(
                myOrdersTabLayout,
                viewPager
            ) { tab, position ->
                tab.text = tabsArray[position]
            }.attach()
        }
    }

    private fun setupListeners() {
        binding.myOrdersBackIconIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}