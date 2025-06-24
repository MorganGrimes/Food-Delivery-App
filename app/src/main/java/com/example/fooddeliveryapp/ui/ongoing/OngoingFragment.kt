package com.example.fooddeliveryapp.ui.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.data.local.database.AppDatabase
import com.example.fooddeliveryapp.data.repository.OrderRepository
import com.example.fooddeliveryapp.databinding.FragmentOngoingBinding
import com.example.fooddeliveryapp.ui.adapters.OrderRecyclerAdapter

class OngoingFragment : Fragment() {

    private lateinit var orderRecyclerAdapter: OrderRecyclerAdapter
    private val viewModel: OngoingViewModel by viewModels {
        OngoingViewModelFactory(
            OrderRepository(AppDatabase.getDatabase(requireContext()).orderDao())
        )
    }

    private var _binding: FragmentOngoingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        viewModel.allOrders.observe(viewLifecycleOwner) { orders ->
            orderRecyclerAdapter.updateData(orders)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerOngoing.apply {
            layoutManager = LinearLayoutManager(requireContext())
            orderRecyclerAdapter = OrderRecyclerAdapter(emptyList())
            adapter = orderRecyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}