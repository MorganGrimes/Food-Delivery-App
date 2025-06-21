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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity
import com.example.fooddeliveryapp.databinding.FragmentAddCardBinding
import com.example.fooddeliveryapp.ui.payment.PaymentViewModel
import com.example.fooddeliveryapp.utils.CREDIT_CARD_ID
import com.example.fooddeliveryapp.utils.FILL_FIELDS
import com.example.fooddeliveryapp.utils.MASTERCARD
import com.example.fooddeliveryapp.utils.UiUtils
import com.example.fooddeliveryapp.utils.VISA

class AddCardFragment : Fragment() {

    private val viewModel: PaymentViewModel by activityViewModels()
    private var currentCardId: Int = -1
    private var selectedCardName: String = ""
    private var fullCardNumber: String = ""

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            addCardCardNumberEt.addTextChangedListener {
                val number = it.toString().trim()
                selectedCardName = when {
                    number.startsWith("4") -> VISA
                    number.startsWith("5") -> MASTERCARD
                    else -> getString(R.string.master_card)
                }
                if (number.length in 1..15) {
                    addCardCardNumberEt.error = getString(R.string.number_must_be_16)
                } else {
                    addCardCardNumberEt.error = null
                }
            }
            creditCardExpireDateValidation()
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
                    isExpanded = false,
                    balance = 100.5
                )

                if (currentCardId != -1) {
                    viewModel.update(card)
                } else {
                    viewModel.insert(card)
                }

                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.addCardFragment, true)
                    .build()

                findNavController().navigate(R.id.paymentFragment, null, navOptions)
            }
        }
    }

    private fun checkFormValidity() {
        binding.apply {
            val cardNumber = addCardCardNumberEt.text?.toString()?.filter { it.isDigit() } ?: ""

            val valid = addCardCardHolderNameEt.text?.isNotBlank() == true &&
                    cardNumber.length == 16 &&
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
        return when (cardName) {
            MASTERCARD -> R.drawable.mastercard
            VISA -> R.drawable.visa
            else -> R.drawable.ic_launcher_background
        }
    }

    private fun creditCardExpireDateValidation() {
        binding.apply {
            addCardExpireDateCardEt.addTextChangedListener(object : TextWatcher {
                private var previousText = ""
                private var isFormatting = false

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    previousText = s?.toString() ?: ""
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (isFormatting) return
                    isFormatting = true

                    val raw = s.toString().replace("/", "")
                    val formatted = StringBuilder()

                    for (i in raw.indices) {
                        if (i == 2) formatted.append("/")
                        formatted.append(raw[i])
                    }

                    val current = addCardExpireDateCardEt.text.toString()
                    if (formatted.toString() != current) {
                        addCardExpireDateCardEt.removeTextChangedListener(this)
                        s?.replace(0, s.length, formatted.toString())
                        addCardExpireDateCardEt.addTextChangedListener(this)
                    }

                    if (raw.length >= 4) {
                        val mm = raw.substring(0, 2).toIntOrNull()
                        val yyyy = raw.substring(2).toIntOrNull()

                        val now = java.util.Calendar.getInstance()
                        val currentMonth = now.get(java.util.Calendar.MONTH) + 1
                        val currentYear = now.get(java.util.Calendar.YEAR)

                        val validMonth = mm != null && mm in 1..12
                        val validDate = yyyy != null &&
                                (yyyy > currentYear || (yyyy == currentYear && mm!! >= currentMonth))

                        when {
                            !validMonth -> addCardExpireDateCardEt.error = getString(R.string.month_not_valid)
                            !validDate -> addCardExpireDateCardEt.error = getString(R.string.year_not_valid)
                            else -> addCardExpireDateCardEt.error = null
                        }
                    } else {
                        addCardExpireDateCardEt.error = null
                    }

                    isFormatting = false
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        fullCardNumber = ""
    }
}
