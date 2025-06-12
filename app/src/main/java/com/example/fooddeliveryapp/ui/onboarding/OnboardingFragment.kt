package com.example.fooddeliveryapp.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.OnboardingItemModel
import com.example.fooddeliveryapp.databinding.FragmentOnBoardingBinding
import com.example.fooddeliveryapp.ui.adapters.OnboardingAdapter
import com.example.fooddeliveryapp.utils.ProfileSharedPreferences

class OnboardingFragment : Fragment() {

    private lateinit var onboardingItemList: List<OnboardingItemModel>
    private lateinit var onboardingAdapter: OnboardingAdapter

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setupListener()
        onSelectedPage()
    }

    private fun initRecyclerView() {
        binding.apply {
            onboardingItemList = listOf(
                OnboardingItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.onboarding_title),
                    getString(R.string.onboarding_description)
                ),
                OnboardingItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.onboarding_title),
                    getString(R.string.onboarding_description)
                ),
                OnboardingItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.onboarding_second_title),
                    getString(R.string.onboarding_description)
                ),
                OnboardingItemModel(
                    R.drawable.ic_launcher_background,
                    getString(R.string.onboarding_third_title),
                    getString(R.string.onboarding_description)
                )
            )
            onboardingAdapter = OnboardingAdapter(onboardingItemList)
            onboardingVp.adapter = onboardingAdapter
            dotsIndicator.attachTo(onboardingVp)
        }
    }

    private fun setupListener() {
        binding.apply {
            onboardingNextBtn.setOnClickListener {
                val current = onboardingVp.currentItem
                if (current < onboardingItemList.size - 1) {
                    onboardingVp.currentItem = current + 1
                } else {
                    completeOnboarding()
                    it.findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
                }
            }

            onboardingSkipTv.setOnClickListener {
                completeOnboarding()
                it.findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            }
        }
    }

    private fun onSelectedPage() {
        binding.apply {
            onboardingVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == onboardingItemList.size - 1) {
                        onboardingNextBtn.text = getString(R.string.get_started)
                        onboardingSkipTv.visibility = View.INVISIBLE
                    } else {
                        onboardingNextBtn.text = getString(R.string.next)
                        onboardingSkipTv.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun completeOnboarding() {
        ProfileSharedPreferences.setOnboardingCompleted(requireContext(), true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}