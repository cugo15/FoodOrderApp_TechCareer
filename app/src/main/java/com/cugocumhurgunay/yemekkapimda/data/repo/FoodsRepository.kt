package com.cugocumhurgunay.yemekkapimda.data.repo

import com.cugocumhurgunay.yemekkapimda.data.datasource.FoodsDataSource
import com.cugocumhurgunay.yemekkapimda.data.entity.BasketItems
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods

class FoodsRepository(var fds: FoodsDataSource) {

    suspend fun loadFoods() : List<Foods> = fds.loadFoods()

    suspend fun loadFoodsByPrice(descending: Boolean): List<Foods> = fds.loadFoodsByPrice(descending)

    suspend fun loadFoodsByName(descending: Boolean): List<Foods> = fds.loadFoodsByName(descending)

    suspend fun searchFoods(searchingWord: String): List<Foods>
            = fds.searchFoods(searchingWord)

    suspend fun addToBasket(food_name:String, food_img:String,
                            food_price:Int, food_number:Int) =
        fds.addToBasket(food_name, food_img, food_price, food_number)

    suspend fun loadBasket() : List<BasketItems> = fds.loadBasket()

    suspend fun deleteFromBasket(basket_food_id:Int) = fds.deleteFromBasket(basket_food_id)

    suspend fun clearBasket() = fds.clearBasket()
}