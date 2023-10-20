package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main

import android.app.Activity
import android.content.Context
import android.location.Location
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(var arepo: AuthRepository) : ViewModel(){
    var userProfileLiveData = MutableLiveData<UserProfile>()

    init {
        loadProfile()
    }

    fun cikisYap(a: Activity, targetActivity: Class<*>){
        arepo.cikisYap(a, targetActivity)
    }

    fun loadProfile(){
        userProfileLiveData = arepo.loadProfile()
    }
    fun updateUser(user_name:String,user_surname:String,user_adress:String){
        arepo.updateUser(user_name, user_surname,user_adress)
    }
    fun updateUserImg(selectedPicture: Uri){
        arepo.updateUserImg(selectedPicture)
    }
    fun getLocation(locationTask: Task<Location>, mContext: Context, editText: EditText){
        arepo.getLocation(locationTask, mContext, editText)
    }
}