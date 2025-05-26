package com.example.fooddeliveryapp.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.fooddeliveryapp.data.model.OnboardingItemModel
import com.example.fooddeliveryapp.databinding.ItemOnBoardingBinding

class OnboardingAdapter(private val items: List<OnboardingItemModel>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(private val binding: ItemOnBoardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItemModel) {
            binding.apply {
                onboardingImageIv.setImageResource(item.image)
                onboardingTitleTv.text = item.title
                onboardingDescriptionTv.text = item.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view =
            ItemOnBoardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}