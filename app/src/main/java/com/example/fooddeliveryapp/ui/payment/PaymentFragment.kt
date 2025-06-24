package com.example.fooddeliveryapp.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.PaypalWebViewActivity
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.database.AppDatabase
import com.example.fooddeliveryapp.data.local.entity.OrderEntity
import com.example.fooddeliveryapp.data.repository.OrderRepository
import com.example.fooddeliveryapp.databinding.FragmentPaymentBinding
import com.example.fooddeliveryapp.ui.adapters.CreditCardRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.ui.ongoing.OngoingViewModel
import com.example.fooddeliveryapp.ui.ongoing.OngoingViewModelFactory
import com.example.fooddeliveryapp.utils.CREDIT_CARD_ID
import com.example.fooddeliveryapp.utils.FOOD
import com.example.fooddeliveryapp.utils.MASTERCARD_CAMELCASE
import com.example.fooddeliveryapp.utils.ONGOING
import com.example.fooddeliveryapp.utils.PAYMENT_FAILED
import com.example.fooddeliveryapp.utils.PAYMENT_SUCCESS
import com.example.fooddeliveryapp.utils.URL
import com.example.fooddeliveryapp.utils.VISA_CAMELCASE
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import com.example.fooddeliveryapp.data.model.CartItemModel
import com.example.fooddeliveryapp.data.remote.PaymentVerificationRequest
import com.example.fooddeliveryapp.data.remote.RetrofitInstance
import com.example.fooddeliveryapp.utils.PAYMENT_VERIFICATION_FAILED
import com.example.fooddeliveryapp.utils.UNKNOWN_ERROR
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private lateinit var creditCardRecyclerAdapter: CreditCardRecyclerAdapter
    private val viewModel: PaymentViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var ongoingViewModel: OngoingViewModel
    private var selectedPaymentMethod: String? = null
    private var selectedCreditCardId: Int? = null

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

        setupInitializeOrder()
        setupObserver()
        setupListener()
        setupRecyclerView()
    }

    private fun setupInitializeOrder() {
        val orderDao = AppDatabase.getDatabase(requireContext()).orderDao()
        val orderRepository = OrderRepository(orderDao)
        val factory = OngoingViewModelFactory(orderRepository)
        ongoingViewModel = ViewModelProvider(this, factory)[OngoingViewModel::class.java]
    }

    private fun setupObserver() {
        binding.apply {
            homeViewModel.cartTotalPrice.observe(viewLifecycleOwner) { total ->
                paymentTotalPriceTv.text =
                    String.format(Locale.getDefault(), "$%.2f", total)
            }

            viewModel.allCreditCards.observe(viewLifecycleOwner) {
                selectedPaymentMethod?.let { type ->
                    if (type.equals(VISA_CAMELCASE, true) || type.equals(
                            MASTERCARD_CAMELCASE,
                            true
                        )
                    ) {
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
                onPaymentSuccess()
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
                },
                onCardSelected = { card ->
                    selectedCreditCardId = card.id
                    creditCardRecyclerAdapter.setSelectedCard(card.id)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_card, card.creditCardNumbers.takeLast(3)),
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun onPaymentSuccess() {
        val totalPrice = homeViewModel.cartTotalPrice.value ?: 0.0
        val cartItems = homeViewModel.cartItems.value?.filter {
            it.cartId == homeViewModel.cartId.value
        } ?: emptyList()

        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), R.string.cart_is_empty, Toast.LENGTH_SHORT).show()
            return
        }

        binding.paymentProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val response = RetrofitInstance.paymentApi.verifyPayment(
                PaymentVerificationRequest(
                    paymentMethod = selectedPaymentMethod ?: "unknown",
                    amount = totalPrice,
                    cardId = selectedCreditCardId
                )
            )
            kotlinx.coroutines.delay(1500)
            binding.paymentProgressBar.visibility = View.GONE

            if (response.isSuccessful && response.body()?.success == true) {
                proceedWithOrder(totalPrice, cartItems)
            } else {
                Toast.makeText(
                    requireContext(),
                    PAYMENT_VERIFICATION_FAILED + (response.body()?.message ?: UNKNOWN_ERROR),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun proceedWithOrder(totalPrice: Double, cartItems: List<CartItemModel>) {
        if (selectedPaymentMethod == VISA_CAMELCASE || selectedPaymentMethod == MASTERCARD_CAMELCASE) {
            val selectedCard =
                viewModel.allCreditCards.value?.find { it.id == selectedCreditCardId }

            if (selectedCard == null || selectedCard.balance < totalPrice) {
                Toast.makeText(requireContext(), R.string.insufficient_balance, Toast.LENGTH_SHORT)
                    .show()
                return
            }

            val updatedCard = selectedCard.copy(balance = selectedCard.balance - totalPrice)
            viewModel.updateCreditCard(updatedCard)
        }

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.paymentFragment, true)
            .build()
        findNavController().navigate(R.id.paymentSuccessfullFragment, null, navOptions)

        val restaurantId = cartItems.first().restaurantId
        val itemCount = "${cartItems.size} item${if (cartItems.size > 1) "s" else ""}"

        val restaurantName = homeViewModel.getAllRestaurants()
            .firstOrNull { it.id == restaurantId }
            ?.name ?: "Unknown Restaurant"

        val currentDate = java.text.SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
            .format(java.util.Date())
        val orderId = (100000..999999).random().toString()

        val newOrder = OrderEntity(
            orderCategoryTypeName = FOOD,
            orderStatus = ONGOING,
            orderImage = R.drawable.ic_launcher_background,
            orderSellerName = restaurantName,
            orderPrice = String.format(Locale.getDefault(), "$%.2f", totalPrice),
            orderDate = currentDate,
            orderItemNumber = itemCount,
            orderId = orderId
        )

        ongoingViewModel.insertOrder(newOrder)

        homeViewModel.cartItems.value = mutableListOf()
        homeViewModel.updateCartTotalPrice()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PAYPAL_REQUEST_CODE = 1001
    }
}