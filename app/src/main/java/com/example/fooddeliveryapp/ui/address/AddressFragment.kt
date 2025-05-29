package com.example.fooddeliveryapp.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.data.model.AddressItemModel
import com.example.fooddeliveryapp.data.model.PopularFoodItemModel
import com.example.fooddeliveryapp.databinding.FragmentAddressBinding
import com.example.fooddeliveryapp.ui.adapters.AddressRecyclerAdapter

class AddressFragment : Fragment() {

    private lateinit var addressRecyclerAdapter: AddressRecyclerAdapter

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupRecyclerView()
    }

    private fun setupListener() {
        binding.addressAddNewAddressBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addressFragment_to_addNewAddressFragment)
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val address = listOf(
                AddressItemModel(
                    R.drawable.home,
                    getString(R.string.home),
                    getString(R.string._2464_royal_ln_mesa_new_jersey_45463)
                )
            )

            addressRecyclerAdapter = AddressRecyclerAdapter(address)

            recyclerAddress.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = addressRecyclerAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}