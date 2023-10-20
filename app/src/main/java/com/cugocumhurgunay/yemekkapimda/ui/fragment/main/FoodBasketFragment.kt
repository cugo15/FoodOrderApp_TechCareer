package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentFoodBasketBinding
import com.cugocumhurgunay.yemekkapimda.ui.adapter.BasketAdapter
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.FoodBasketViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodBasketFragment : Fragment() {
    private lateinit var binding: FragmentFoodBasketBinding
    private lateinit var viewModel: FoodBasketViewModel
    var adress = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFoodBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userProfileLiveData.observe(viewLifecycleOwner){
            if(it != null){
                binding.textViewBasketTeslimatAdresi.text = "Teslimat adresi : ${it.user_adress}"
                adress = it.user_adress!!
            }
        }

        viewModel.basketList.observe(viewLifecycleOwner){
            binding.rvFoodBasket.layoutManager = LinearLayoutManager(requireContext())
            val basketAdapter = BasketAdapter(requireContext(),it,viewModel)
            binding.rvFoodBasket.adapter = basketAdapter
            binding.textViewTotalPrice.text = "${viewModel.orderTotalPrice()}TL"

            val orderDetails = StringBuilder()
            it.forEach { item ->
                val c =
                    "${item.yemek_adi}(${item.yemek_siparis_adet} Adet) : ${(item.yemek_fiyat * item.yemek_siparis_adet)} TL\n"
                orderDetails.append(c)

            }
            binding.buttonCompleteOrder.setOnClickListener {
                if (viewModel.orderTotalPrice().toInt()>0){
                    val order_time = Timestamp.now()
                    viewModel.addOrder(order_time,orderDetails.toString(),adress,viewModel.totalPrice)
                    Navigation.goTo(it,R.id.basketToOrder)
                    viewModel.clearBasket()
                }else {
                    val snackbar = Snackbar.make(it,
                        "Lütfen sepete ürün ekleyiniz",
                        Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
        }
        binding.imageViewGoHome.setOnClickListener {
            Navigation.goTo(it,R.id.basketToHome)
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FoodBasketViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBasket()
    }

}