package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddeliveryapp.data.local.entity.CreditCardEntity
import com.example.fooddeliveryapp.databinding.RecyclerCreditCardLayoutBinding

class CreditCardRecyclerAdapter(
    private var items: List<CreditCardEntity>,
    private val onEditClicked: (CreditCardEntity) -> Unit,
    private val onDeleteClicked: (CreditCardEntity) -> Unit,
    private val onCardSelected: (CreditCardEntity) -> Unit,
    private var selectedCardId: Int? = null
) : RecyclerView.Adapter<CreditCardRecyclerAdapter.CreditCardViewHolder>() {

    fun updateData(newItems: List<CreditCardEntity>) {
        val resetItems = newItems.map { it.copy(isExpanded = false) }

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = resetItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == resetItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == resetItems[newItemPosition]
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = resetItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun setSelectedCard(id: Int?) {
        val previousSelected = selectedCardId
        selectedCardId = id

        notifyItemChanged(items.indexOfFirst { it.id == previousSelected })
        notifyItemChanged(items.indexOfFirst { it.id == selectedCardId })
    }

    inner class CreditCardViewHolder(private val binding: RecyclerCreditCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CreditCardEntity) {
            binding.apply {
                recyclerCreditCardNameTv.text = item.creditCardName
                recyclerCreditCardIconIv.setImageResource(item.creditCardImage)
                recyclerCreditCardLastThreeNumberPinTv.text =
                    if (item.creditCardNumbers.length >= 3) {
                        item.creditCardNumbers.takeLast(3)
                    } else {
                        item.creditCardNumbers
                    }
                recyclerCreditCardHolderNameTv.text = item.creditCardHolderName
                recyclerCreditCardExpireDateTv.text = item.creditCardExpireDate
                recyclerCreditCardCvcTv.text = item.creditCardCvc

                recyclerCreditCardHolderNameTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardExpireDateTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardCvcTv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardEditIv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE
                recyclerCreditCardDeleteIv.visibility =
                    if (item.isExpanded) View.VISIBLE else View.GONE

                recyclerCreditCardDeleteIv.setOnClickListener { onDeleteClicked(item) }
                recyclerCreditCardEditIv.setOnClickListener { onEditClicked(item) }

                recyclerCreditCardEditArrow.setOnClickListener {
                    item.isExpanded = !item.isExpanded
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        notifyItemChanged(position)
                    }
                }

                root.setOnClickListener {
                    onCardSelected(item)
                }
                root.isSelected = item.id == selectedCardId
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