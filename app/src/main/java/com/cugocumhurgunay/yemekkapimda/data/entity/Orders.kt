package com.cugocumhurgunay.yemekkapimda.data.entity

import com.google.firebase.Timestamp
import java.io.Serializable

data class Orders (var order_time: Timestamp,
                   var order_details:String,
                   var order_adress:String,
                   var order_total:Int) : Serializable {
}