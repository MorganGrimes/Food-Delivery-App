package com.example.fooddeliveryapp.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.CreditCardItemModel
import com.example.fooddeliveryapp.databinding.FragmentPaymentBinding
import com.example.fooddeliveryapp.ui.adapters.CreditCardRecyclerAdapter

class PaymentFragment : Fragment() {

    private lateinit var creditCardRecyclerAdapter: CreditCardRecyclerAdapter

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupRecyclerView()
    }

    private fun setupListener() {
        binding.apply {
            paymentBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
            paymentAddNewBtn.setOnClickListener {
                findNavController().navigate(R.id.action_paymentFragment_to_addCardFragment)
            }
            placeOrderBtn.setOnClickListener {
                findNavController().navigate(R.id.action_paymentFragment_to_paymentSuccessfullFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val creditCard = listOf(
                CreditCardItemModel(
                    getString(R.string.master_card),
                    R.drawable.mastercard,
                    getString(R.string._345),
                    getString(R.string.vishal_khadok),
                    getString(R.string.mm_yyyy),
                    getString(R.string.cvc)
                ), CreditCardItemModel(
                    getString(R.string.visa),
                    R.drawable.visa,
                    getString(R.string._345),
                    getString(R.string.vishal_khadok),
                    getString(R.string.mm_yyyy),
                    getString(R.string.cvc)
                )
            )

            creditCardRecyclerAdapter = CreditCardRecyclerAdapter(creditCard)

            recyclerCreditCard.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = creditCardRecyclerAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}