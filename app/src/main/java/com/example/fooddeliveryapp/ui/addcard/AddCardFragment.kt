package com.example.fooddeliveryapp.ui.addcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity
import com.example.fooddeliveryapp.databinding.FragmentAddCardBinding
import com.example.fooddeliveryapp.ui.payment.PaymentViewModel
import com.example.fooddeliveryapp.utils.CREDIT_CARD_ID
import com.example.fooddeliveryapp.utils.FILL_FIELDS
import com.example.fooddeliveryapp.utils.UiUtils

class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by activityViewModels()
    private var currentCardId: Int = -1
    private var selectedCardName: String = ""
    private var fullCardNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        setupCard()
        setupListeners()
    }

    private fun setupCard() {
        val cardId = arguments?.getInt(CREDIT_CARD_ID, -1) ?: -1
        currentCardId = cardId
        if (currentCardId != -1) {
            viewModel.allCreditCards.observe(viewLifecycleOwner) { cards ->
                val card = cards.find { it.id == currentCardId }
                card?.let {
                    populateFields(it)
                }
            }
        }
    }

    private fun populateFields(card: CreditCardEntity) {
        binding.apply {
            addCardCardHolderNameEt.setText(card.creditCardHolderName)
            addCardCardNumberEt.setText(card.creditCardNumbers)
            addCardExpireDateCardEt.setText(card.creditCardExpireDate)
            addCardCvcEt.setText(card.creditCardCvc)
        }
    }

    private fun setupListeners() {
        binding.apply {
            val inputs = listOf(
                addCardCardHolderNameEt,
                addCardCardNumberEt,
                addCardExpireDateCardEt,
                addCardCvcEt
            )

            inputs.forEach {
                it.addTextChangedListener { checkFormValidity() }
            }

            addEMakePaymentBtn.isEnabled = false
            addEMakePaymentBtn.setBackgroundColor(UiUtils.brownColor)

            addEMakePaymentBtn.setOnClickListener {
                val holderName = addCardCardHolderNameEt.text.toString().trim()
                val cardNumber = addCardCardNumberEt.text.toString().trim()
                val expireDate = addCardExpireDateCardEt.text.toString().trim()
                val cvc = addCardCvcEt.text.toString().trim()

                if (holderName.isEmpty() || cardNumber.isEmpty() || expireDate.isEmpty() || cvc.isEmpty()) {
                    Toast.makeText(requireContext(), FILL_FIELDS, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val cardImage = getCardImageResByName(selectedCardName)

                val card = CreditCardEntity(
                    id = if (currentCardId != -1) currentCardId else 0,
                    creditCardHolderName = holderName,
                    creditCardName = selectedCardName.ifEmpty { getString(R.string.master_card) },
                    creditCardNumbers = cardNumber,
                    creditCardExpireDate = expireDate,
                    creditCardCvc = cvc,
                    creditCardImage = cardImage,
                    isExpanded = false
                )

                if (currentCardId != -1) {
                    viewModel.update(card)
                } else {
                    viewModel.insert(card)
                }

                findNavController().navigate(R.id.action_addCardFragment_to_paymentFragment)
            }
        }
    }

    private fun checkFormValidity() {
        binding.apply {
            val valid = addCardCardHolderNameEt.text?.isNotBlank() == true &&
                    addCardCardNumberEt.text?.isNotBlank() == true &&
                    addCardExpireDateCardEt.text?.isNotBlank() == true &&
                    addCardCvcEt.text?.isNotBlank() == true

            addEMakePaymentBtn.isEnabled = valid
            addEMakePaymentBtn.setBackgroundColor(
                if (valid) resources.getColor(R.color.orange, null)
                else (UiUtils.brownColor)
            )
        }
    }

    private fun getCardImageResByName(cardName: String): Int {
        return when (cardName.lowercase()) {
            "mastercard" -> R.drawable.mastercard
            "visa" -> R.drawable.visa
            "paypal" -> R.drawable.paypal
            else -> R.drawable.mastercard
        }
    }

    private fun getArgs() {
        arguments?.getString("creditCardName")?.let {
            selectedCardName = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fullCardNumber = ""
    }
}
