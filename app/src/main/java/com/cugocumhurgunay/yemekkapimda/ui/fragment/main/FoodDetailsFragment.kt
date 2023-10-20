package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentFoodDetailsBinding
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.FoodDetailsViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFoodDetailsBinding
    private lateinit var viewModel: FoodDetailsViewModel
    private var num = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle:FoodDetailsFragmentArgs by navArgs()
        val food = bundle.food

        val backButton = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Navigation.goTo(binding.imageViewDetailsClose,R.id.detailsToHome)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backButton)

        viewModel.favoriteIcon(food.yemek_adi)

        viewModel.favLiveData.observe(viewLifecycleOwner){
            if(it){
                binding.imageViewDetailsFavNotSelected.visibility = View.GONE
                binding.imageViewDetailsFavSelected.visibility = View.VISIBLE
            }else{
                binding.imageViewDetailsFavNotSelected.visibility = View.VISIBLE
                binding.imageViewDetailsFavSelected.visibility = View.GONE
            }
        }

        binding.textViewDetailsName.text = food.yemek_adi
        binding.textViewDetailsPrice.text = "${food.yemek_fiyat} TL"
        binding.textViewDetailsTotalPrice.text = "${(num*food.yemek_fiyat)} TL"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"
        showImgGlide(url,binding.imageViewFoodDetails,requireContext(),256,256)

        binding.buttonPlus.setOnClickListener {
            num++
            binding.textViewNumber.text = num.toString()
            binding.textViewDetailsTotalPrice.text = "${(num*food.yemek_fiyat)} TL"
        }
        binding.buttonMinus.setOnClickListener {
            if(num > 1){
                num--
                binding.textViewNumber.text = num.toString()
                binding.textViewDetailsTotalPrice.text = "${(num*food.yemek_fiyat)} TL"
            }
        }

        binding.imageViewDetailsClose.setOnClickListener {
            Navigation.goTo(it,R.id.detailsToHome)
        }
        binding.buttonBasket.setOnClickListener {
            addToBasket(food.yemek_adi,food.yemek_resim_adi,food.yemek_fiyat,num)
            Navigation.goTo(it,R.id.detailsToBasket)
        }


        binding.imageViewDetailsFavNotSelected.setOnClickListener {
            viewModel.addFavoriteFood(food.yemek_id,food.yemek_adi,food.yemek_resim_adi,food.yemek_fiyat)
            binding.imageViewDetailsFavSelected.visibility = View.VISIBLE
            binding.imageViewDetailsFavNotSelected.visibility = View.GONE
        }

        binding.imageViewDetailsFavSelected.setOnClickListener {
            viewModel.deleteFavFood(food.yemek_adi)
            binding.imageViewDetailsFavSelected.visibility = View.GONE
            binding.imageViewDetailsFavNotSelected.visibility = View.VISIBLE
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FoodDetailsViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun addToBasket(food_name:String, food_img:String,
                    food_price:Int, food_number:Int){
        viewModel.addToBasket(food_name, food_img, food_price, food_number)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBasket()
    }

}