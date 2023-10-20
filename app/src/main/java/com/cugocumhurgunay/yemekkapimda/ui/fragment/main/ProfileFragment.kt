package com.cugocumhurgunay.yemekkapimda.ui.fragment.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.cugocumhurgunay.yemekkapimda.R
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentProfileBinding
import com.cugocumhurgunay.yemekkapimda.ui.activity.LoginActivity
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.main.ProfileViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.cugocumhurgunay.yemekkapimda.utils.showImgGlide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var izinKontrol = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewProfileClose.setOnClickListener {
            Navigation.goTo(it,R.id.profileToHome)
        }
        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.textInputLayoutAdres.setStartIconOnClickListener {
            val izinKontrol = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )

            if (izinKontrol == PackageManager.PERMISSION_GRANTED) {
                locationTask = flpc.lastLocation
                getLocation()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }
        }

        binding.imageViewOpenGallery.setOnClickListener {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
                if(ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES)!= PackageManager.PERMISSION_GRANTED){
                    //permission denied
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES)){
                        Snackbar.make(it, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                            View.OnClickListener {
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }).show()
                    }else{
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
                else{
                    //permission granted
                    val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                }
            }

            else{
                if(ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    //permission denied
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Snackbar.make(it, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                            View.OnClickListener {
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }).show()
                    }else{
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
                else{
                    //permission granted
                    val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }
        }

        binding.buttonLogout.setOnClickListener {
            viewModel.cikisYap(requireActivity(), LoginActivity::class.java)
        }

        viewModel.userProfileLiveData.observe(viewLifecycleOwner){
            if(it != null){
                binding.editTextAd.setText(it.user_name)
                binding.editTextSoyad.setText(it.user_surname)
                binding.editTextAdres.setText(it.user_adress)
                binding.editTextMail.setText(it.user_email)
                showImgGlide(it.user_img,binding.imageViewProfilePicture,requireContext(),300,300)
            }
        }

        binding.buttonUpdate.setOnClickListener {
            if (binding.editTextAd.text.toString().isNotEmpty()
                &&binding.editTextSoyad.text.toString().isNotEmpty()
                &&binding.editTextAdres.text.toString().isNotEmpty()){
                viewModel.updateUser(binding.editTextAd.text.toString(),binding.editTextSoyad.text.toString(),binding.editTextAdres.text.toString())
            }else{
                Snackbar.make(it,
                    "Lütfen boş alanları doldurunuz",
                    Snackbar.LENGTH_SHORT).show()
            }
            selectedPicture?.let { it1 ->
                viewModel.updateUserImg(it1)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProfileViewModel by viewModels()
        viewModel = tempViewModel
        registerLauncherImg()
    }

    private fun registerLauncherImg(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode== AppCompatActivity.RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult!=null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.imageViewProfilePicture.setImageURI(it)
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->
            if(result){
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(requireContext(),"Permission needed!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            izinKontrol = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationTask = flpc.lastLocation
                getLocation()
                Toast.makeText(requireContext(),"Onaylandı", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Onaylanmadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getLocation(){
        viewModel.getLocation(locationTask,requireContext(),binding.editTextAdres)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProfile()
    }


}