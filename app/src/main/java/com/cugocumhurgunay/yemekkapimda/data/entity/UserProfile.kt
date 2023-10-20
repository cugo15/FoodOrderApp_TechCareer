package com.cugocumhurgunay.yemekkapimda.data.entity

import java.io.Serializable

class UserProfile (var user_email:String? = "",
                   var user_name:String? = "",
                   var user_surname:String? = "",
                   var user_adress:String? = "",
                   var user_img:String? = "") : Serializable {
}