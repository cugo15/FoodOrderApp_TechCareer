package com.cugocumhurgunay.yemekkapimda.retrofit

import com.cugocumhurgunay.yemekkapimda.data.entity.BasketRespond
import com.cugocumhurgunay.yemekkapimda.data.entity.CRUDRespond
import com.cugocumhurgunay.yemekkapimda.data.entity.FoodsRespond
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodsDao {
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun loadFoods() : FoodsRespond


    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addToBasket(@Field("yemek_adi")yemek_adi:String,
                            @Field("yemek_resim_adi") yemek_resim_adi:String,
                            @Field("yemek_fiyat") yemek_fiyat:Int,
                            @Field("yemek_siparis_adet") yemek_siparis_adet:Int,
                            @Field("kullanici_adi") kullanici_adi:String
    ) : CRUDRespond

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun loadBasket(@Field("kullanici_adi") kullanici_adi:String) : BasketRespond


    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun deleteFromBasket(@Field ("sepet_yemek_id")sepet_yemek_id:Int,
                                 @Field("kullanici_adi") kullanici_adi:String) : CRUDRespond

}