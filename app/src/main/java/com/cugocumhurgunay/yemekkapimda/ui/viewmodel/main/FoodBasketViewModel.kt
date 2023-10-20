package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.BasketItems
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.cugocumhurgunay.yemekkapimda.data.repo.FoodsRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodBasketViewModel @Inject constructor(var arepo: AuthRepository,
                                              var frepo: FoodsRepository) : ViewModel()  {

    var userProfileLiveData = MutableLiveData<UserProfile>()
    var basketList = MutableLiveData<List<BasketItems>>()
    var totalPrice = 0

    init {
        loadBasket()
        loadProfile()
    }

    fun loadProfile(){
        userProfileLiveData = arepo.loadProfile()
    }

    fun loadBasket() {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                basketList.value = frepo.loadBasket()

            }catch (e:Exception){
            }
        }
    }
    fun deleteFromBasket(basket_food_id: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val currentBasket = basketList.value ?: emptyList()
            val itemIndex = currentBasket.indexOfFirst { it.sepet_yemek_id == basket_food_id }
            if (itemIndex != -1) {
                val updatedBasket = currentBasket.toMutableList()
                updatedBasket.removeAt(itemIndex)
                basketList.value = updatedBasket
                if (updatedBasket.isEmpty()) {
                    basketList.value = emptyList()
                }
            }
            frepo.deleteFromBasket(basket_food_id)
            loadBasket()
        }
    }
    fun orderTotalPrice(): String{
        var total = 0
        basketList.value?.forEach {
            total += it.yemek_siparis_adet * it.yemek_fiyat
        }
        totalPrice = total
        return totalPrice.toString()
    }
    fun addOrder(timestamp: Timestamp, order_details:String, order_adress:String, order_total:Int){
        arepo.addOrder(timestamp, order_details, order_adress, order_total)
    }
    fun clearBasket() {
        CoroutineScope(Dispatchers.Main).launch {
            frepo.clearBasket()
        }
    }

}