package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentOrdersBinding
import com.cugocumhurgunay.yemekkapimda.ui.adapter.OrderAdapter
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.OrderViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewOrderClose.setOnClickListener {
            Navigation.goTo(it,R.id.orderToHome)
        }
        binding.rvOrder.layoutManager = LinearLayoutManager(requireContext())
        val popupMenu = PopupMenu(requireContext(), binding.imageViewOrderMenu)
        popupMenu.menuInflater.inflate(R.menu.orders_order_menu, popupMenu.menu)
        binding.imageViewOrderMenu.setOnClickListener {
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.artanFiyat -> {
                    viewModel.loadOrdersBySort("order_total", Query.Direction.ASCENDING)
                    true
                }
                R.id.azalanFiyat -> {
                    viewModel.loadOrdersBySort("order_total", Query.Direction.DESCENDING)
                    true
                }
                R.id.eskidenYeniye -> {
                    viewModel.loadOrdersBySort("order_time", Query.Direction.ASCENDING)
                    true
                }
                R.id.yenidenEskiye -> {
                    viewModel.loadOrdersBySort("order_time", Query.Direction.DESCENDING)
                    true
                }
                else -> false
            }
        }

        viewModel.orderList.observe(viewLifecycleOwner){
            val orderAdapter = OrderAdapter(requireContext(),it,viewModel)
            binding.rvOrder.adapter = orderAdapter

        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: OrderViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.orders_order_menu, menu)
    }



}