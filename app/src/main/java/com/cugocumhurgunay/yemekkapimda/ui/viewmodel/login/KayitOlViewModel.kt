package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.login

import android.app.Activity
import android.content.Context
import android.location.Location
import android.net.Uri
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KayitOlViewModel@Inject constructor(var arepo: AuthRepository) : ViewModel() {

    fun kayitOl(mail:String, password:String, name:String, surname:String, adress:String, selectedPicture: Uri,
                mContext: Context, a: Activity,
                targetActivity: Class<*>){
        arepo.kayitOl(mail, password, name, surname, adress, selectedPicture, mContext, a, targetActivity)
    }
    fun getLocation(locationTask: Task<Location>, mContext: Context, editText: EditText){
        arepo.getLocation(locationTask, mContext, editText)
    }
}