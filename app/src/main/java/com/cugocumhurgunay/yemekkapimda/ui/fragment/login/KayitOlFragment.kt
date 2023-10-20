package com.cugocumhurgunay.yemekkapimda.ui.fragment.login

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
import com.cugocumhurgunay.yemekkapimda.databinding.FragmentKayitOlBinding
import com.cugocumhurgunay.yemekkapimda.ui.activity.MainActivity
import com.cugocumhurgunay.yemekkapimda.ui.viewmodel.login.KayitOlViewModel
import com.cugocumhurgunay.yemekkapimda.utils.goTo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class KayitOlFragment : Fragment() {
    private lateinit var binding: FragmentKayitOlBinding
    private lateinit var viewModel: KayitOlViewModel
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
        binding = FragmentKayitOlBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener {
            Navigation.goTo(it,R.id.kayitToGiris)
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
        binding.imageViewProfile.setOnClickListener {
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

        binding.buttonKayitOl.setOnClickListener {

            val eMail = binding.editTextMailKayit.text.toString()
            val password = binding.editTextSifreKayit.text.toString()
            val password2 = binding.editTextSifreKayit2.text.toString()
            val name = binding.editTextName.text.toString()
            val surname = binding.editTextSurname.text.toString()
            val adres = binding.editTextAdress.text.toString()

            selectedPicture?.let { it1 ->
                controlFields(eMail,password,password2,name,surname,adres,
                    it1,it)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: KayitOlViewModel by viewModels()
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
                        binding.imageViewProfile.setImageURI(it)
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
            izinKontrol = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationTask = flpc.lastLocation
                getLocation()
                Toast.makeText(requireContext(),"Onaylandı",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Onaylanmadı",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getLocation(){
        viewModel.getLocation(locationTask,requireContext(),binding.editTextAdress)
    }
    private fun controlFields(eMail:String,password:String,password2:String,name:String,surname:String,adres:String,selectedPic:Uri,view: View) {
        if (eMail.isEmpty()) {
            Snackbar.make(
                view,
                "Mail Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT
            ).show()
        } else if (password.length < 6 || password.length>16) {
            Snackbar.make(
                view,
                "Şifre 6 ila 16 Karakter Arasında Olmalıdır",
                Snackbar.LENGTH_SHORT
            ).show()
        }else if (password2.length < 6 || password2.length>16) {
            Snackbar.make(
                view,
                "Şifre 6 ila 16 Karakter Arasında Olmalıdır",
                Snackbar.LENGTH_SHORT
            ).show()
        }else if (name.isEmpty()) {
            Snackbar.make(
                view,
                "Ad Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT
            ).show()
        }else if (surname.isEmpty()) {
            Snackbar.make(
                view,
                "Soyad Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT
            ).show()
        }else if (eMail.isEmpty()) {
            Snackbar.make(
                view,
                "Mail Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        else if (adres.isEmpty()) {
            Snackbar.make(
                view,
                "Adres Alanı Boş Bırakılamaz",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        else if (selectedPic == null) {
            Snackbar.make(
                view,
                "Lütfen Bir Profil Fotoğrafı Yükleyiniz",
                Snackbar.LENGTH_SHORT
            ).show()
        }else if (password!=password2) {
            Snackbar.make(
                view,
                "Şifreleriniz eşleşmiyor !!",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        else{
            selectedPicture?.let { it1 ->
                viewModel.kayitOl(eMail, password,name,surname,adres,
                    it1,requireContext(),requireActivity(), MainActivity::class.java)
            }
            binding.progressKayit.visibility = View.VISIBLE
        }
    }


}