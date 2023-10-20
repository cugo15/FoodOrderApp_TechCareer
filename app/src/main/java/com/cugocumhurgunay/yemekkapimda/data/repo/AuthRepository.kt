package com.cugocumhurgunay.yemekkapimda.data.repo

import android.app.Activity
import android.content.Context
import android.location.Location
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.cugocumhurgunay.yemekkapimda.data.datasource.AuthDataSource
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.data.entity.Orders
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query

class AuthRepository(var ads: AuthDataSource){

    fun kayitOl(mail:String, password:String, name:String, surname:String, adress:String, selectedPicture: Uri,
                mContext: Context, a: Activity,
                targetActivity: Class<*>)
            = ads.kayitOl(mail, password, name, surname, adress, selectedPicture, mContext, a, targetActivity)

    fun girisYap(mail:String,password:String,mContext: Context,a:Activity,targetActivity: Class<*>)
            = ads.girisYap(mail, password, mContext,a,targetActivity)

    fun updateUser(user_name:String,user_surname:String,user_adress:String) =ads.updateUser(user_name, user_surname,user_adress)

    fun updateUserImg(selectedPicture: Uri) = ads.updateUserImg(selectedPicture)

    fun dahaOnceGirisYapildimi(a: Activity,targetActivity: Class<*>)
            = ads.dahaOnceGirisYapildimi(a,targetActivity)

    fun cikisYap(a: Activity,targetActivity: Class<*>) = ads.cikisYap(a,targetActivity)

    fun getLocation(locationTask: Task<Location>, mContext: Context, editText: EditText) = ads.getLocation(locationTask, mContext, editText)

    fun loadProfile() : MutableLiveData<UserProfile> = ads.loadProfile()

    fun addFavoriteFood(food_id:Int,food_name:String,food_img:String,food_price:Int)
            = ads.addFavoriteFood(food_id, food_name, food_img, food_price)

    fun loadFavoriteFoods() : MutableLiveData<List<Foods>> = ads.loadFavoriteFoods()

    fun loadFavoriteFoodsBySort(order : String,sortDirection: Query.Direction ) : MutableLiveData<List<Foods>>
            = ads.loadFavoriteFoodsBySort(order, sortDirection)

    fun searchFavoriteFoods(searchingWord:String): MutableLiveData<List<Foods>> = ads.searchFavoriteFoods(searchingWord)

    fun favIcon(food_name: String): Boolean = ads.favIcon(food_name)

    fun favoriteIcon(food_name: String): MutableLiveData<Boolean> = ads.favoriteIcon(food_name)

    fun deleteFavFood(food_name:String) = ads.deleteFavFood(food_name)

    fun addOrder(timestamp: Timestamp, order_details:String, order_adress:String, order_total:Int)
            = ads.addOrder(timestamp, order_details, order_adress, order_total)

    fun loadOrders() : MutableLiveData<List<Orders>> = ads.loadOrders()

    fun loadOrdersBySort(order : String,sortDirection: Query.Direction ): MutableLiveData<List<Orders>>
    = ads.loadOrdersBySort(order, sortDirection)


}