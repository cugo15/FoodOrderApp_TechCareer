package com.cugocumhurgunay.yemekkapimda.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentGirisYapBinding
import com.cugocumhurgunay.yemekkapimda.ui.activity.MainActivity
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.login.GirisYapViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GirisYapFragment : Fragment() {
    private lateinit var binding: FragmentGirisYapBinding
    private lateinit var viewModel: GirisYapViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGirisYapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewKayitOl.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.girisToKayit)
        }
        binding.buttonGirisYap.setOnClickListener {
            val eMail = binding.editTextMailGiris.text.toString()
            val password = binding.editTextSifreGiris.text.toString()
            controlFields(eMail,password,it)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: GirisYapViewModel by viewModels()
        viewModel = tempViewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.dahaOnceGirisYapildimi(requireActivity(), MainActivity::class.java)
    }
    private fun controlFields(eMail:String,password:String,view: View){
        if(eMail.isEmpty()){
            Snackbar.make(view,
                "Mail Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Snackbar.make(view,
                "Şifre Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT).show()
        }
        else{
            viewModel.girisYap(eMail, password,
                requireContext(),requireActivity(),MainActivity::class.java)
            binding.progressGiris.visibility = View.VISIBLE
        }
    }


}