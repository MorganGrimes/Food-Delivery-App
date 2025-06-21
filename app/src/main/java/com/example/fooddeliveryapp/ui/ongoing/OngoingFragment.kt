package com.example.fooddeliveryapp.ui.ongoing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.OrderItemModel
import com.example.fooddeliveryapp.databinding.FragmentOngoingBinding
import com.example.fooddeliveryapp.ui.adapters.OrderRecyclerAdapter
import com.example.fooddeliveryapp.ui.home.HomeViewModel

class OngoingFragment : Fragment() {

    private lateinit var orderRecyclerAdapter: OrderRecyclerAdapter
    private val viewModel: HomeViewModel by activityViewModels()

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

        viewModel.orderItems.observe(viewLifecycleOwner) { orders ->
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