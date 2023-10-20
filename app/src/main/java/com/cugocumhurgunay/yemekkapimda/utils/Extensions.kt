package com.cugocumhurgunay.yemekkapimda.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

fun Navigation.goTo(it: View, id:Int){
    findNavController(it).navigate(id)
}

fun Navigation.goTo(it: View, id: NavDirections){
    findNavController(it).navigate(id)
}

fun showImgGlide(url:String?, view: ImageView, mContext: Context, width:Int, height:Int){
    Glide.with(mContext).load(url)
        .override(width,height)
        .into(view)
}