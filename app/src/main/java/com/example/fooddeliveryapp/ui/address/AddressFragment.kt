package com.example.fooddeliveryapp.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddeliveryapp.R
import com.example.fooddeliveryapp.databinding.FragmentAddressBinding
import com.example.fooddeliveryapp.ui.adapters.AddressRecyclerAdapter
import com.example.fooddeliveryapp.utils.ADDRESS_ID

class AddressFragment : Fragment() {

    private lateinit var addressRecyclerAdapter: AddressRecyclerAdapter
    private val viewModel: AddressViewModel by activityViewModels()

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
        viewModel.allAddresses.observe(viewLifecycleOwner) { addresses ->
            addressRecyclerAdapter.updateData(addresses)
        }
    }

    private fun setupListener() {
        binding.apply {
            addressAddNewAddressBtn.setOnClickListener {
                findNavController().navigate(R.id.action_addressFragment_to_addNewAddressFragment)
            }
            addressBackIconIv.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        addressRecyclerAdapter = AddressRecyclerAdapter(
            items = emptyList(),
            onEditClicked = { address ->
                val bundle = Bundle().apply {
                    putInt(ADDRESS_ID, address.id)
                }
                findNavController().navigate(R.id.action_addressFragment_to_addNewAddressFragment, bundle)
            },
            onDeleteClicked = { address ->
                viewModel.delete(address)
            }
        )

        binding.recyclerAddress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addressRecyclerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}