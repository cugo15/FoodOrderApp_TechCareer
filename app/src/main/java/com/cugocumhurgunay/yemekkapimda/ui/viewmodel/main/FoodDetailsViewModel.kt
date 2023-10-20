package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.BasketItems
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.cugocumhurgunay.yemekkapimda.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(var arepo: AuthRepository,
                                               var frepo: FoodsRepository
) : ViewModel() {

    var favLiveData = MutableLiveData<Boolean>()
    var basketList = MutableLiveData<List<BasketItems>>()
    val x = MutableLiveData(0)
    val y = MutableLiveData(0)

    init {
        loadBasket()
    }

    fun favoriteIcon(food_name: String) {
        favLiveData = arepo.favoriteIcon(food_name)
    }

    fun addFavoriteFood(food_id:Int,food_name:String,food_img:String,food_price:Int){
        arepo.addFavoriteFood(food_id, food_name, food_img, food_price)
    }
    fun deleteFavFood(food_name:String){
        arepo.deleteFavFood(food_name)
    }

    fun addToBasket(food_name: String, food_img: String, food_price: Int, food_number: Int) {
        CoroutineScope(Dispatchers.Main).launch {

            if (!basketList.value.isNullOrEmpty()) {
                val items = basketList.value!!

                // Check if the item already exists in the basket
                val existingItem = items.find { it.yemek_adi == food_name }
                if (existingItem != null) {
                    x.value = existingItem.yemek_siparis_adet
                    y.value = existingItem.sepet_yemek_id
                    deleteFromBasket(y.value!!)
                    frepo.addToBasket(food_name, food_img, food_price, (food_number + x.value!!))
                }else{
                    frepo.addToBasket(food_name, food_img, food_price, (food_number))
                }
            }else{
                frepo.addToBasket(food_name, food_img, food_price, (food_number))
            }
            x.value = 0
            y.value = 0
            loadBasket()
        }
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




}