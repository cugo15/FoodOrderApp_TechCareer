package com.cugocumhurgunay.yemekkapimda.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.databinding.FoodsRowBinding
import com.cugocumhurgunay.yemekkapimda.ui.fragment.main.HomeFragmentDirections
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.HomeViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide

class FoodsAdapter (var mContext: Context,
                    var foodsList:List<Foods>,
                    var viewModel: HomeViewModel
)
    : RecyclerView.Adapter<FoodsAdapter.FoodsHolder>(){

    inner class FoodsHolder(var binding: FoodsRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsHolder {
        val binding = FoodsRowBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return FoodsHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    override fun onBindViewHolder(holder: FoodsHolder, position: Int) {
        val food = foodsList[position]
        val t = holder.binding
        t.textViewFoodName.text = food.yemek_adi
        t.textViewFoodPrice.text = "${food.yemek_fiyat} TL"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"
        showImgGlide(url,t.imageViewFood,mContext,256,256)
        t.cvFood.setOnClickListener {
            val goTo = HomeFragmentDirections.homeToDetails(food = food)
            Navigation.goTo(it,goTo)
        }

        if(viewModel.favIcon(food.yemek_adi)){
            t.imageViewFoodAddFavorite.setImageResource(R.drawable.fav)
        }else{
            t.imageViewFoodAddFavorite.setImageResource(R.drawable.favborder)
        }
    }
}