package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.model.CreditCardItemModel
import com.example.fooddeliveryapp.databinding.RecyclerCreditCardLayoutBinding

class CreditCardRecyclerAdapter(
    private val items: List<CreditCardItemModel>,
) : RecyclerView.Adapter<CreditCardRecyclerAdapter.CreditCardViewHolder>() {

    inner class CreditCardViewHolder(private val binding: RecyclerCreditCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CreditCardItemModel) {
            binding.apply {
                recyclerCreditCardNameTv.text = item.creditCardName
                recyclerCreditCardIconIv.setImageResource(item.creditCardImage)
                recyclerCreditCardLastThreeNumberPinTv.text = item.creditCardLastNumbers
                recyclerCreditCardHolderNameTv.text = item.creditCardHolderName
                recyclerCreditCardExpireDateTv.text = item.creditCardExpireDate
                recyclerCreditCardCvcTv.text = item.creditCardCvc

                recyclerCreditCardHolderNameTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardExpireDateTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardCvcTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE

                recyclerCreditCardEditArrow.setOnClickListener {
                    item.isExpanded = !item.isExpanded
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val view = RecyclerCreditCardLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CreditCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}