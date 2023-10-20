package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.Orders
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(var arepo: AuthRepository) : ViewModel(){

    var orderList = MutableLiveData<List<Orders>>()

    init {
        loadOrders()
    }

    fun loadOrders(){
        orderList = arepo.loadOrders()
    }
    fun loadOrdersBySort(order : String,sortDirection: Query.Direction ){
        orderList = arepo.loadOrdersBySort(order, sortDirection)
    }
}