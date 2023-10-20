package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteFoodsViewModel @Inject constructor(var arepo: AuthRepository) : ViewModel(){

    var favoriteFoodsList = MutableLiveData<List<Foods>>()
    var userProfileLiveData = MutableLiveData<UserProfile>()

    init {
        loadFavoriteFoods()
        loadProfile()
    }

    fun loadFavoriteFoods(){
        favoriteFoodsList = arepo.loadFavoriteFoods()
    }
    fun searchFavoriteFoods(searchingWord:String){
        favoriteFoodsList = arepo.searchFavoriteFoods(searchingWord)
    }
    fun deleteFavFood(food_name:String){
        arepo.deleteFavFood(food_name)
    }
    fun loadFavoriteFoodsBySort(order : String,sortDirection: Query.Direction ) {
        favoriteFoodsList = arepo.loadFavoriteFoodsBySort(order, sortDirection)
    }
    fun loadProfile(){
        userProfileLiveData = arepo.loadProfile()
    }
}