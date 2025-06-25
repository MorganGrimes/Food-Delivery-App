package com.example.fooddeliveryapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.local.database.AppDatabase
import com.example.fooddeliveryapp.data.model.OrderItemModel
import com.example.fooddeliveryapp.data.repository.OrderRepository
import com.example.fooddeliveryapp.databinding.FragmentHistoryBinding
import com.example.fooddeliveryapp.ui.adapters.OrderRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel
import com.example.fooddeliveryapp.ui.ongoing.OngoingViewModel
import com.example.fooddeliveryapp.ui.ongoing.OngoingViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var orderRecyclerAdapter: OrderRecyclerAdapter
    private val viewModel: OngoingViewModel by viewModels {
        OngoingViewModelFactory(
            OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao())
        )
    }

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

        viewModel.historyOrders.observe(viewLifecycleOwner) { orders ->
            orderRecyclerAdapter.updateData(orders)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.apply {

            recyclerHistory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                orderRecyclerAdapter = OrderRecyclerAdapter(emptyList())
                adapter = orderRecyclerAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}