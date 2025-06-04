package com.example.fooddeliveryapp.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentPaymentBinding
import com.example.fooddeliveryapp.ui.adapters.CreditCardRecyclerAdapter
import com.example.fooddeliveryapp.utils.CREDIT_CARD_ID

class PaymentFragment : Fragment() {

    private lateinit var creditCardRecyclerAdapter: CreditCardRecyclerAdapter
    private val viewModel: PaymentViewModel by activityViewModels()

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
        observeCreditCards()
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

            creditCardRecyclerAdapter = CreditCardRecyclerAdapter(
                items = emptyList(),
                onEditClicked = { card ->
                    val bundle = Bundle().apply {
                        putInt(CREDIT_CARD_ID, card.id)
                    }
                    findNavController().navigate(
                        R.id.action_paymentFragment_to_addCardFragment,
                        bundle
                    )
                },
                onDeleteClicked = { card ->
                    viewModel.delete(card)
                }
            )

            recyclerCreditCard.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = creditCardRecyclerAdapter
            }
        }
    }

    private fun observeCreditCards() {
        viewModel.allCreditCards.observe(viewLifecycleOwner) { cards ->
            creditCardRecyclerAdapter.updateData(cards)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}