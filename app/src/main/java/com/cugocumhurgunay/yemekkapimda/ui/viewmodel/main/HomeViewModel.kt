package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.cugocumhurgunay.yemekkapimda.data.repo.FoodsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(var arepo: AuthRepository,
                                        var frepo: FoodsRepository) : ViewModel(){

    var userProfileLiveData = MutableLiveData<UserProfile>()
    var foodlist = MutableLiveData<List<Foods>>()
    var favoriteFoodsList = MutableLiveData<List<Foods>>()

    init {
        loadFoods()
        loadProfile()
        loadFavoriteFoods()
    }

    fun loadFavoriteFoods(){
        favoriteFoodsList = arepo.loadFavoriteFoods()
    }
    fun favIcon(food_name: String): Boolean {
        return arepo.favIcon(food_name)
    }
    fun loadProfile(){
        userProfileLiveData = arepo.loadProfile()
    }

    fun loadFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                foodlist.value = frepo.loadFoods()
            }catch (e:Exception){

            }
        }
    }
    fun loadFoodsByPrice(descending: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                foodlist.value = frepo.loadFoodsByPrice(descending)
            }catch (e:Exception){

            }
        }
    }
    fun loadFoodsByName(descending: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try{
                foodlist.value = frepo.loadFoodsByName(descending)
            }catch (e:Exception){

            }
        }
    }

    fun searchFoods(searchingWord:String){
        CoroutineScope(Dispatchers.Main).launch {
            try{
                foodlist.value = frepo.searchFoods(searchingWord)
            }catch (e:Exception){}
        }
    }
}