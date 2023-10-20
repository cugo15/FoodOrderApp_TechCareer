package com.cugocumhurgunay.yemekkapimda.ui.viewmodel.login

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GirisYapViewModel @Inject constructor(var arepo: AuthRepository) : ViewModel(){

    fun girisYap(mail:String, password:String, mContext: Context, a: Activity, targetActivity: Class<*>){
        arepo.girisYap(mail,password,mContext,a,targetActivity)
    }
    fun dahaOnceGirisYapildimi(a: Activity,targetActivity: Class<*>){
        arepo.dahaOnceGirisYapildimi(a,targetActivity)
    }
}