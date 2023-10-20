package com.cugocumhurgunay.yemekkapimda.data.datasource

import android.util.Log
import com.cugocumhurgunay.yemekkapimda.data.entity.BasketItems
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.retrofit.FoodsDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodsDataSource (var fdao: FoodsDao, var auth: FirebaseAuth) {

    suspend fun loadFoods(): List<Foods> = withContext(Dispatchers.IO) {
        return@withContext fdao.loadFoods().yemekler
    }

    suspend fun loadFoodsByPrice(descending: Boolean): List<Foods> = withContext(Dispatchers.IO) {
        val foodList = fdao.loadFoods().yemekler
        val sortedFoodList = if (descending) {
            foodList.sortedByDescending { it.yemek_fiyat }
        } else {
            foodList.sortedBy { it.yemek_fiyat }
        }
        return@withContext sortedFoodList
    }
    suspend fun loadFoodsByName(descending: Boolean): List<Foods> = withContext(Dispatchers.IO) {
        val foodList = fdao.loadFoods().yemekler
        val sortedFoodList = if (descending) {
            foodList.sortedByDescending { it.yemek_adi }
        } else {
            foodList.sortedBy { it.yemek_adi }
        }
        return@withContext sortedFoodList
    }

    suspend fun searchFoods(searchingWord: String): List<Foods> = withContext(Dispatchers.IO) {
        val allFoods = fdao.loadFoods().yemekler
        val list = ArrayList<Foods>()

        for (f in allFoods) {
            if (f.yemek_adi.lowercase().contains(searchingWord.lowercase())) {
                list.add(f)
            }
        }
        return@withContext list
    }

    suspend fun addToBasket(food_name: String, food_img: String,
                            food_price: Int, food_number: Int,
    ) {
        fdao.addToBasket(food_name, food_img, food_price, food_number, auth.uid!!)
    }

    suspend fun loadBasket() : List<BasketItems> = withContext(Dispatchers.IO) {
        return@withContext fdao.loadBasket(auth.uid!!).sepet_yemekler
    }

    suspend fun clearBasket() {
        val basketItems = loadBasket() // Load all items from the basket
        for (item in basketItems) {
            val cevap = fdao.deleteFromBasket(item.sepet_yemek_id, auth.uid!!)
            Log.e("Delete from Basket", "Success: ${cevap.success} Message: ${cevap.message}")
        }
    }

    suspend fun deleteFromBasket(basket_food_id:Int){
        val cevap = fdao.deleteFromBasket(basket_food_id,auth.uid!!)
        Log.e("Add to Basket","Success : ${cevap.success} Message : ${cevap.message}")
    }


}