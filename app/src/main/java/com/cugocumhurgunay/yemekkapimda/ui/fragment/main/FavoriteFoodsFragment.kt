package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentFavoriteFoodsBinding
import com.cugocumhurgunay.yemekkapimda.ui.adapter.FavoriteFoodsAdapter
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.FavoriteFoodsViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFoodsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteFoodsBinding
    private lateinit var viewModel: FavoriteFoodsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteFoodsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewCloseFav.setOnClickListener {
            Navigation.goTo(it,R.id.favToHome)
        }
        binding.rvFavoriteFoods.layoutManager = LinearLayoutManager(requireContext())
        val popupMenu = PopupMenu(requireContext(), binding.imageViewSortFav)
        popupMenu.menuInflater.inflate(R.menu.food_sort_menu, popupMenu.menu)
        binding.imageViewSortFav.setOnClickListener {
            popupMenu.show()
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.artanFiyat -> {
                    viewModel.loadFavoriteFoodsBySort("yemek_fiyat", Query.Direction.ASCENDING)
                    true
                }
                R.id.azalanFiyat -> {
                    viewModel.loadFavoriteFoodsBySort("yemek_fiyat", Query.Direction.DESCENDING)
                    true
                }
                R.id.alfabetikArtan -> {
                    viewModel.loadFavoriteFoodsBySort("yemek_adi", Query.Direction.ASCENDING)
                    true
                }
                R.id.alfabetikAzalan -> {
                    viewModel.loadFavoriteFoodsBySort("yemek_adi", Query.Direction.DESCENDING)
                    true
                }
                else -> false
            }
        }
        viewModel.userProfileLiveData.observe(viewLifecycleOwner){
            binding.textViewFavUserName.text = "${it.user_name} İşte En Sevdiklerin"
        }
        viewModel.favoriteFoodsList.observe(viewLifecycleOwner){
            val favoriteFooodsAdapter = FavoriteFoodsAdapter(requireContext(),it,viewModel)
            binding.rvFavoriteFoods.adapter = favoriteFooodsAdapter
        }
        binding.searchViewFavoriteFoods.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchFavoriteFoods(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchFavoriteFoods(query)
                return true
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: FavoriteFoodsViewModel by viewModels()
        viewModel = tempViewModel
    }
}