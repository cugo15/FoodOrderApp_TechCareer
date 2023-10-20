package com.cugocumhurgunay.yemekkapimda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.data.entity.BasketItems
import com.cugocumhurgunay.yemekkapimda.databinding.BasketRowBinding
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.FoodBasketViewModel
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide
import com.google.android.material.snackbar.Snackbar

class BasketAdapter(var mContext: Context,
                    var basketList:List<BasketItems>,
                    var viewModel: FoodBasketViewModel
)  : RecyclerView.Adapter<BasketAdapter.BasketHolder>(){
    inner class BasketHolder(var binding: BasketRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketHolder {
        val binding = BasketRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return BasketHolder(binding)
    }

    override fun getItemCount(): Int {
        return basketList.size
    }

    override fun onBindViewHolder(holder: BasketHolder, position: Int) {
        val foodBasket = basketList[position]
        val t = holder.binding
        t.textViewBasketFoodName.text = foodBasket.yemek_adi
        t.textViewBasketFoodNumber.text = "Adet : ${foodBasket.yemek_siparis_adet}"
        t.textViewBasketFoodPrice.text = "Fiyat : ${foodBasket.yemek_fiyat} TL"
        t.textViewBasketTotalPrice.text = "Toplam : ${(foodBasket.yemek_fiyat*foodBasket.yemek_siparis_adet)} TL"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${foodBasket.yemek_resim_adi}"
        showImgGlide(url,t.imageViewBasketFoodImg,mContext,256,256)
        t.buttonBasketFoodDelete.setOnClickListener {
            Snackbar.make(it,
                "${foodBasket.yemek_adi} Sepetten silinsin mi ?",
                Snackbar.LENGTH_SHORT).setAction("Evet"){
                viewModel.deleteFromBasket(foodBasket.sepet_yemek_id)
            }.setActionTextColor(ContextCompat.getColor(mContext, R.color.mainColor)).show()
        }
    }
}