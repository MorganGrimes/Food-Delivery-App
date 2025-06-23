package com.example.fooddeliveryapp.ui.coupon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentCouponDialogBinding
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CouponDialogFragment : DialogFragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentCouponDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (SharedPreferences.getCouponCloseCount(requireContext()) >= 2) {
            dismiss()
            return
        }
        setupListener()
    }

    private fun setupListener() {
        binding.apply {
            viewModel.coupons.observe(viewLifecycleOwner) { coupons ->
                val now = Date()
                val validCoupon = coupons.firstOrNull { coupon ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val start = sdf.parse(coupon.startDate)
                    val end = sdf.parse(coupon.endDate)
                    start != null && end != null && now.after(start) && now.before(end)
                }

                if (validCoupon != null) {
                    couponCodeTv.text = validCoupon.code
                    couponDescriptionTv.text =
                        getString(
                            R.string.coupon_description_template,
                            validCoupon.discountPercentage
                        )
                } else {
                    couponDescriptionTv.text = ""
                }
            }


            closeIcon.setOnClickListener {
                SharedPreferences.incrementCouponCloseCount(requireContext())
                dismiss()
            }
            gotItBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setDimAmount(0.1F)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}