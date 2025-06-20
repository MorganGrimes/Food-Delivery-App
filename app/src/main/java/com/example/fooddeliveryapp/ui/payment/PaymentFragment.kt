package com.example.fooddeliveryapp.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentPaymentBinding
import com.example.fooddeliveryapp.PaypalWebViewActivity
import com.example.fooddeliveryapp.ui.adapters.CreditCardRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.utils.CREDIT_CARD_ID
import com.example.fooddeliveryapp.utils.MASTERCARD_CAMELCASE
import com.example.fooddeliveryapp.utils.PAYMENT_FAILED
import com.example.fooddeliveryapp.utils.PAYMENT_SUCCESS
import com.example.fooddeliveryapp.utils.URL
import com.example.fooddeliveryapp.utils.VISA_CAMELCASE
import java.util.Locale

class PaymentFragment : Fragment() {

    private lateinit var creditCardRecyclerAdapter: CreditCardRecyclerAdapter
    private val viewModel: PaymentViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var selectedPaymentMethod: String? = null

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

        val showBottomBar = arguments?.getBoolean("showBottomBar", false) ?: false
        binding.paymentBottomCl.visibility = if (showBottomBar) View.VISIBLE else View.GONE

        setupObserver()
        setupListener()
        setupRecyclerView()
    }

    private fun setupObserver() {
        binding.apply {
            homeViewModel.cartTotalPrice.observe(viewLifecycleOwner) { total ->
                paymentTotalPriceTv.text =
                    String.format(Locale.getDefault(), "$%.2f", total)
            }

            viewModel.allCreditCards.observe(viewLifecycleOwner) {
                selectedPaymentMethod?.let { type ->
                    if (type.equals(VISA_CAMELCASE, true) || type.equals(MASTERCARD_CAMELCASE, true)) {
                        filterCardsByType(type)
                        binding.recyclerCreditCard.visibility = View.VISIBLE
                    } else {
                        binding.recyclerCreditCard.visibility = View.GONE
                    }
                }
            }
        }
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
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.paymentFragment, true)
                    .build()

                findNavController().navigate(R.id.paymentSuccessfullFragment, null, navOptions)
            }

            paymentVisaTv.setOnClickListener {
                selectedPaymentMethod = VISA_CAMELCASE
                recyclerCreditCard.visibility = View.VISIBLE
                filterCardsByType(VISA_CAMELCASE)
                paymentVisaTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.visa_selected,
                    0,
                    0
                )
                paymentMastercardTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.mastercard,
                    0,
                    0
                )
                paymentCashTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cash, 0, 0)
                paymentPaypalTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.paypal, 0, 0)
            }

            paymentMastercardTv.setOnClickListener {
                selectedPaymentMethod = MASTERCARD_CAMELCASE
                recyclerCreditCard.visibility = View.VISIBLE
                filterCardsByType(MASTERCARD_CAMELCASE)
                paymentMastercardTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.mastercard_selected,
                    0,
                    0
                )
                paymentVisaTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.visa, 0, 0)
                paymentCashTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cash, 0, 0)
                paymentPaypalTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.paypal, 0, 0)
            }

            paymentCashTv.setOnClickListener {
                selectedPaymentMethod = "cash"
                recyclerCreditCard.visibility = View.GONE
                noCreditCardCw.visibility = View.GONE
                paymentMastercardTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.mastercard,
                    0,
                    0
                )
                paymentVisaTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.visa, 0, 0)
                paymentCashTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.cash_selected,
                    0,
                    0
                )
                paymentPaypalTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.paypal, 0, 0)
            }

            paypalPayment()
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

    private fun paypalPayment() {
        binding.apply {
            paymentPaypalTv.setOnClickListener {
                selectedPaymentMethod = "paypal"
                recyclerCreditCard.visibility = View.GONE
                noCreditCardCw.visibility = View.GONE
                paymentMastercardTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.mastercard,
                    0,
                    0
                )
                paymentVisaTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.visa, 0, 0)
                paymentCashTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.cash,
                    0,
                    0
                )
                paymentPaypalTv.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.paypal_selected,
                    0,
                    0
                )
                val paypalPaymentUrl =
                    "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=YOUR_TOKEN_HERE"

                val intent = Intent(requireContext(), PaypalWebViewActivity::class.java)
                intent.putExtra(URL, paypalPaymentUrl)
                startActivityForResult(intent, PAYPAL_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE) {
            val paymentSuccess = data?.getBooleanExtra(PAYMENT_SUCCESS, false) ?: false
            if (paymentSuccess) {
                findNavController().navigate(R.id.paymentSuccessfullFragment)
            } else {
                Toast.makeText(requireContext(), PAYMENT_FAILED, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterCardsByType(type: String) {
        viewModel.allCreditCards.value?.let { cards ->
            val filtered = cards.filter { it.creditCardName.equals(type, ignoreCase = true) }
            creditCardRecyclerAdapter.updateData(filtered)
            binding.noCreditCardCw.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PAYPAL_REQUEST_CODE = 1001
    }
}