package com.cugocumhurgunay.yemekkapimda.data.entity

import java.io.Serializable

data class BasketItems(var sepet_yemek_id:Int,
                       var yemek_adi:String,
                       var yemek_resim_adi:String,
                       var yemek_fiyat:Int,
                       var yemek_siparis_adet:Int,
                       var kullanici_adi:String) : Serializable {
}