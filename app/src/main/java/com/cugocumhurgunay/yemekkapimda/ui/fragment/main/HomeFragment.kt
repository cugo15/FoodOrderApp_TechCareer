package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentHomeBinding
import com.cugocumhurgunay.yemekkapimda.ui.adapter.FoodsAdapter
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.HomeViewModel
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userProfileLiveData.observe(viewLifecycleOwner){
            if(it != null){
                binding.textViewWelcome.text = "Acıktın mı ${it.user_name} ?"
                showImgGlide(it.user_img,binding.imageViewUserHome,requireContext(),400,400)
            }
        }


        binding.rvHome.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        viewModel.foodlist.observe(viewLifecycleOwner){
            val foodsAdapter = FoodsAdapter(requireContext(),it,viewModel)
            binding.rvHome.adapter = foodsAdapter
        }

        val popupMenu = PopupMenu(requireContext(), binding.imageViewSortHome)
        popupMenu.menuInflater.inflate(R.menu.food_sort_menu, popupMenu.menu)
        binding.imageViewSortHome.setOnClickListener {
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.artanFiyat -> {
                    viewModel.loadFoodsByPrice(false)
                    true
                }
                R.id.azalanFiyat -> {
                    viewModel.loadFoodsByPrice(true)
                    true
                }
                R.id.alfabetikArtan -> {
                    viewModel.loadFoodsByName(false)
                    true
                }
                R.id.alfabetikAzalan -> {
                    viewModel.loadFoodsByName(true)
                    true
                }
                else -> false
            }
        }

        binding.searchViewHome.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {//Harf girdikçe ve sildikçe
                viewModel.searchFoods(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {//Arama iconuna bastığında
                searchFoods(query)
                return true
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun searchFoods(searchingWord:String){
        viewModel.searchFoods(searchingWord)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFoods()
    }


}