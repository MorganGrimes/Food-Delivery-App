package com.example.fooddeliveryapp.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : DialogFragment() {

    private var selectedDeliveryTimeRange: IntRange? = null
    private var selectedPricingRange: ClosedFloatingPointRange<Double>? = null
    private var selectedMinRating: Int? = null

    private var _binding: FragmentFilterDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDeliveryTimeButtons()
        setupPricingButtons()
        setupRatingButtons()

        binding.filterBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putIntArray("deliveryTimeRange", selectedDeliveryTimeRange?.toList()?.toIntArray() ?: intArrayOf())
                putDoubleArray("pricingRange", doubleArrayOf(
                    selectedPricingRange?.start ?: -1.0,
                    selectedPricingRange?.endInclusive ?: -1.0
                ))
                putInt("minRating", selectedMinRating ?: -1)
            }
            parentFragmentManager.setFragmentResult("filterRequestKey", bundle)
            dismiss()
        }

    }

    private fun setupDeliveryTimeButtons() {
        binding.apply {
            tenFifteenMinBtn.setOnClickListener {
                selectedDeliveryTimeRange = 10..15
                updateDeliveryTimeButtonsUI()
            }
            twentyMinBtn.setOnClickListener {
                selectedDeliveryTimeRange = 20..20
                updateDeliveryTimeButtonsUI()
            }
            thirtyMinBtn.setOnClickListener {
                selectedDeliveryTimeRange = 30..30
                updateDeliveryTimeButtonsUI()
            }
        }
    }

    private fun updateDeliveryTimeButtonsUI() {
        binding.apply {
            updateDeliveryTimeButtonUI(tenFifteenMinBtn, selectedDeliveryTimeRange == (10..15))
            updateDeliveryTimeButtonUI(twentyMinBtn, selectedDeliveryTimeRange == (20..20))
            updateDeliveryTimeButtonUI(thirtyMinBtn, selectedDeliveryTimeRange == (30..30))
        }
    }

    private fun updateDeliveryTimeButtonUI(button: com.google.android.material.button.MaterialButton, isSelected: Boolean) {
        if (isSelected) {
            button.setBackgroundColor(resources.getColor(R.color.orange, null))
            button.setTextColor(resources.getColor(android.R.color.white, null))
        } else {
            button.setBackgroundColor(resources.getColor(android.R.color.white, null))
            button.setTextColor(resources.getColor(R.color.black_light, null))
        }
    }

    private fun setupPricingButtons() {
        binding.filterFirstPricingBtn.setOnClickListener {
            selectedPricingRange = 0.0..5.0
            updatePricingButtonsUI()
        }
        binding.filterSecondPricingBtn.setOnClickListener {
            selectedPricingRange = 5.0..10.0
            updatePricingButtonsUI()
        }
        binding.filterThirdPricingBtn.setOnClickListener {
            selectedPricingRange = 10.0..Double.MAX_VALUE
            updatePricingButtonsUI()
        }
    }

    private fun updatePricingButtonsUI() {
        binding.apply {
            updatePricingUI(filterFirstPricingBtn, selectedPricingRange == (0.0..5.0), 1)
            updatePricingUI(filterSecondPricingBtn, selectedPricingRange == (5.0..10.0), 2)
            updatePricingUI(filterThirdPricingBtn, selectedPricingRange == (10.0..Double.MAX_VALUE), 3)
        }
    }

    private fun updatePricingUI(view: ImageView, isSelected: Boolean, pricingIndex: Int) {
        val defaultRes = when(pricingIndex) {
            1 -> R.drawable.dollar_1
            2 -> R.drawable.dollar_2
            3 -> R.drawable.dollar_3
            else -> R.drawable.dollar_1
        }
        val selectedRes = when(pricingIndex) {
            1 -> R.drawable.dollar_1_orange
            2 -> R.drawable.dollar_2_orange
            3 -> R.drawable.dollar_3_orange
            else -> R.drawable.dollar_1_orange
        }

        if (isSelected) {
            view.setImageResource(selectedRes)
        } else {
            view.setImageResource(defaultRes)
        }
    }

    private fun setupRatingButtons() {
        binding.apply {
            filterFirstStarRatingIv.setOnClickListener { selectRating(1) }
            filterSecondStarRatingIv.setOnClickListener { selectRating(2) }
            filterThirdStarRatingIv.setOnClickListener { selectRating(3) }
            filterFourthStarRatingIv.setOnClickListener { selectRating(4) }
            filterFifthStarRatingIv.setOnClickListener { selectRating(5) }
        }
    }

    private fun selectRating(stars: Int) {
        selectedMinRating = stars
        updateRatingButtonsUI()
    }

    private fun updateRatingButtonsUI() {
        binding.apply {
            updateStarUI(filterFirstStarRatingIv, (selectedMinRating ?: 0) >= 1)
            updateStarUI(filterSecondStarRatingIv, (selectedMinRating ?: 0) >= 2)
            updateStarUI(filterThirdStarRatingIv, (selectedMinRating ?: 0) >= 3)
            updateStarUI(filterFourthStarRatingIv, (selectedMinRating ?: 0) >= 4)
            updateStarUI(filterFifthStarRatingIv, (selectedMinRating ?: 0) >= 5)
        }
    }

    private fun updateStarUI(star: ImageView, isSelected: Boolean) {
        if (isSelected) {
            star.setImageResource(R.drawable.rating_star_orange)
        } else {
            star.setImageResource(R.drawable.rating_star)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
