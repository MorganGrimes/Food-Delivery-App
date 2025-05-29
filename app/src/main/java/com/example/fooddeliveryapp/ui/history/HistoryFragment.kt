package com.example.fooddeliveryapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.OrderItemModel
import com.example.fooddeliveryapp.databinding.FragmentHistoryBinding
import com.example.fooddeliveryapp.ui.adapters.OrderRecyclerAdapter

class HistoryFragment : Fragment() {

    private lateinit var orderRecyclerAdapter: OrderRecyclerAdapter

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {
            val orders = listOf(
                OrderItemModel(
                    getString(R.string.food),
                    getString(R.string.completed),
                    R.drawable.ic_launcher_background,
                    getString(R.string.mcdonald),
                    getString(R.string._35_25),
                    getString(R.string._30_jan_12_30),
                    getString(R.string._01_items),
                    getString(R.string._162432)
                ),
                OrderItemModel(
                    getString(R.string.food),
                    getString(R.string.completed),
                    R.drawable.ic_launcher_background,
                    getString(R.string.mcdonald),
                    getString(R.string._35_25),
                    getString(R.string._30_jan_12_30),
                    getString(R.string._01_items),
                    getString(R.string._162432)
                )
            )

            orderRecyclerAdapter = OrderRecyclerAdapter(orders)

            recyclerHistory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = orderRecyclerAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}