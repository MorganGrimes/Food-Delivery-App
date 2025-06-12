package com.example.fooddeliveryapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter (fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

    private var fragmentList: ArrayList<Fragment> = ArrayList()

    fun addFragmentToList(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}