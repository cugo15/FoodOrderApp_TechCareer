package com.cugocumhurgunay.yemekkapimda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.databinding.FavoriteFoodsRowBinding
import com.cugocumhurgunay.yemekkapimda.ui.fragment.main.FavoriteFoodsFragmentDirections
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.FavoriteFoodsViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide
import com.google.android.material.snackbar.Snackbar

class FavoriteFoodsAdapter (var mContext: Context,
                            var foodsList:List<Foods>,
                            var viewModel: FavoriteFoodsViewModel
) : RecyclerView.Adapter<FavoriteFoodsAdapter.FavoriteFoodsHolder>(){
    inner class FavoriteFoodsHolder(var binding: FavoriteFoodsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteFoodsHolder {
        val binding = FavoriteFoodsRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return FavoriteFoodsHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    override fun onBindViewHolder(holder: FavoriteFoodsHolder, position: Int) {
        val favoriteFood = foodsList[position]
        val t = holder.binding
        t.textViewFavoriteFoodName.text = favoriteFood.yemek_adi
        t.textViewFavoriteFoodPrice.text = "${favoriteFood.yemek_fiyat} TL"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${favoriteFood.yemek_resim_adi}"
        showImgGlide(url,t.imageViewFavoriteFoods,mContext,256,256)

        t.cardViewFavFoods.setOnClickListener {
            val goTo = FavoriteFoodsFragmentDirections.favoriteToDetails(food = favoriteFood)
            Navigation.goTo(it,goTo)
        }
        t.imageViewDeleteFavoriteFood.setOnClickListener {
            Snackbar.make(it,
                "${favoriteFood.yemek_adi} Favorilerden silinsin mi ?",
                Snackbar.LENGTH_SHORT).setAction("Evet"){
                viewModel.deleteFavFood(favoriteFood.yemek_adi)
            }.setActionTextColor(ContextCompat.getColor(mContext, R.color.mainColor)).show()

        }

    }

}