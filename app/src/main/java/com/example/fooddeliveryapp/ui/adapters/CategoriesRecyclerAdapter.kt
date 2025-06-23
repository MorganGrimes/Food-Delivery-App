package com.example.fooddeliveryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.CategoriesItemModel
import com.example.fooddeliveryapp.databinding.RecyclerCategoriesLayoutBinding

class CategoriesRecyclerAdapter(
    private var categoriesList: List<CategoriesItemModel>,
    private val onItemClick: (CategoriesItemModel) -> Unit
) : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(private val binding: RecyclerCategoriesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoriesItemModel) {
            binding.apply {
                recyclerCategoriesName.text = item.foodName
                val imageUrl = item.foodImage.replace("localhost", "192.168.178.102")
                recyclerCategoriesIv.load(imageUrl) {
                    decoderFactory(SvgDecoder.Factory())
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_background)
                }
                root.setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = RecyclerCategoriesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.bind(categoriesList[position])
    }

    override fun getItemCount(): Int = categoriesList.size

    fun updateList(newList: List<CategoriesItemModel>) {
        val diffCallback = CategoriesDiffCallback(categoriesList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        categoriesList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class CategoriesDiffCallback(
        private val oldList: List<CategoriesItemModel>,
        private val newList: List<CategoriesItemModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].foodName == newList[newItemPosition].foodName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
