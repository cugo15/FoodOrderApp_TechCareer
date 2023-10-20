package com.cugocumhurgunay.yemekkapimda.data.datasource

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.cugocumhurgunay.yemekkapimda.data.entity.Foods
import com.cugocumhurgunay.yemekkapimda.data.entity.Orders
import com.cugocumhurgunay.yemekkapimda.data.entity.UserProfile
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AuthDataSource (var auth: FirebaseAuth, var reference: StorageReference, var collectionReference: CollectionReference){

    var userProfileLiveData = MutableLiveData<UserProfile>()
    var favoriteFoodsList = MutableLiveData<List<Foods>>()
    var orderList = MutableLiveData<List<Orders>>()
    var favLiveData = MutableLiveData<Boolean>()


    fun kayitOl(mail:String, password:String, name:String, surname:String, adress:String, selectedPicture: Uri,
                mContext: Context, a: Activity,
                targetActivity: Class<*>){

        auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener{
            if(it.isSuccessful){
                auth.currentUser

                if(selectedPicture !=null){
                    val uuid= UUID.randomUUID()
                    val imageName = "$uuid.jpg"
                    reference.child(imageName).putFile(selectedPicture).addOnSuccessListener {
                        reference.child(imageName).downloadUrl.addOnSuccessListener {
                            val downloadUrl = it.toString()
                            val newUser = UserProfile(mail,name,surname,adress,downloadUrl)

                            collectionReference.document(auth.currentUser!!.uid).set(newUser).addOnSuccessListener {
                                updateUI(a,targetActivity)

                            }.addOnFailureListener{
                                //Delete atabilirsin hata önlemek için
                                Toast.makeText(mContext,it.localizedMessage,Toast.LENGTH_SHORT).show()
                            }

                        }.addOnFailureListener{
                            Toast.makeText(mContext,it.localizedMessage,Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(mContext,it.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }.addOnFailureListener {
            Toast.makeText(mContext, it.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }

    fun girisYap(mail:String,password:String,mContext: Context,a:Activity,targetActivity: Class<*>){
        auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener{
            if(it.isSuccessful){
                auth.currentUser
                updateUI(a,targetActivity)
            }
        }.addOnFailureListener {
            Toast.makeText(mContext, it.localizedMessage,Toast.LENGTH_SHORT).show()
        }
    }

    fun updateUserImg(selectedPicture: Uri){
        if(selectedPicture !=null){
            val uuid= UUID.randomUUID()
            val imageName = "$uuid.jpg"
            reference.child(imageName).putFile(selectedPicture).addOnSuccessListener {
                reference.child(imageName).downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val updatedUser = HashMap<String,Any>()
                    updatedUser["user_img"] = downloadUrl
                    collectionReference.document(auth.currentUser!!.uid).update(updatedUser)
                }
            }
        }
    }

    fun updateUser(user_name:String,user_surname:String,user_adress:String){
        val updatedUser = HashMap<String,Any>()
        updatedUser["user_name"] = user_name
        updatedUser["user_surname"] = user_surname
        updatedUser["user_adress"] = user_adress
        collectionReference.document(auth.currentUser!!.uid).update(updatedUser)
    }

    fun loadProfile() : MutableLiveData<UserProfile> {
        collectionReference.document(auth.currentUser!!.uid).get().addOnSuccessListener {
            if(it != null){
                val name = it.data?.get("user_name").toString()
                val surname = it.data?.get("user_surname").toString()
                val mail = it.data?.get("user_email").toString()
                val adress = it.data?.get("user_adress").toString()
                val img = it.data?.get("user_img").toString()

                userProfileLiveData.value = UserProfile(mail,name,surname,adress,img)
            }
        }.addOnFailureListener {
            userProfileLiveData.value = UserProfile("","","","","")
        }
        return userProfileLiveData
    }

    fun loadFavoriteFoods() : MutableLiveData<List<Foods>> {
        collectionReference.document(auth.currentUser!!.uid).collection("FavoriteFoods")
            .addSnapshotListener { value, error ->
            if(value != null){
                val list = ArrayList<Foods>()
                for(document in value.documents){
                    if(document != null){
                        val food_id = document.get("yemek_id").toString()
                        val food_name = document.get("yemek_adi").toString()
                        val food_img = document.get("yemek_resim_adi").toString()
                        val food_price = document.get("yemek_fiyat").toString()
                        val favoriteFood = Foods(food_id.toInt(),food_name,food_img,food_price.toInt())
                        list.add(favoriteFood)
                    }
                }
                favoriteFoodsList.value = list
            }
        }
        return favoriteFoodsList
    }

    fun loadFavoriteFoodsBySort(order : String,sortDirection: Query.Direction ) : MutableLiveData<List<Foods>> {
        collectionReference.document(auth.currentUser!!.uid).collection("FavoriteFoods")
            .orderBy(order, sortDirection)
            .addSnapshotListener { value, error ->
            if(value != null){
                val list = ArrayList<Foods>()
                for(document in value.documents){
                    if(document != null){
                        val food_id = document.get("yemek_id").toString()
                        val food_name = document.get("yemek_adi").toString()
                        val food_img = document.get("yemek_resim_adi").toString()
                        val food_price = document.get("yemek_fiyat").toString()
                        val favoriteFood = Foods(food_id.toInt(),food_name,food_img,food_price.toInt())
                        list.add(favoriteFood)
                    }
                }
                favoriteFoodsList.value = list
            }
        }
        return favoriteFoodsList
    }

    fun searchFavoriteFoods(searchingWord:String): MutableLiveData<List<Foods>> {
        collectionReference.document(auth.currentUser!!.uid).collection("FavoriteFoods").addSnapshotListener { value, error ->
            if(value != null){
                val list = ArrayList<Foods>()
                for(document in value.documents){
                    if(document != null){
                        if(document.get("yemek_adi").toString().lowercase().contains(searchingWord.lowercase())){
                            val food_id = document.get("yemek_id").toString()
                            val food_name = document.get("yemek_adi").toString()
                            val food_img = document.get("yemek_resim_adi").toString()
                            val food_price = document.get("yemek_fiyat").toString()
                            val favoriteFood = Foods(food_id.toInt(),food_name,food_img,food_price.toInt())
                            list.add(favoriteFood)
                        }
                    }
                }
                favoriteFoodsList.value = list
            }
        }
        return favoriteFoodsList
    }

    fun addFavoriteFood(food_id:Int,food_name:String,food_img:String,food_price:Int){
        val currentFavoriteFoods = loadFavoriteFoods().value ?: emptyList()
        val foodAlreadyExists = currentFavoriteFoods.any { it.yemek_adi == food_name}
        if (!foodAlreadyExists) {
            val favoriteFood = Foods(food_id, food_name, food_img, food_price)
            collectionReference.document(auth.currentUser!!.uid)
                .collection("FavoriteFoods")
                .document(food_name)
                .set(favoriteFood)
        }
    }

    fun favIcon(food_name: String): Boolean {
        val currentFavoriteFoods = loadFavoriteFoods().value ?: emptyList()
        return currentFavoriteFoods.any { it.yemek_adi == food_name }
    }

    fun favoriteIcon(food_name: String): MutableLiveData<Boolean> {
        val currentFavoriteFoods = loadFavoriteFoods().value ?: emptyList()
        favLiveData.value = currentFavoriteFoods.any { it.yemek_adi == food_name }
        return favLiveData
    }

    fun deleteFavFood(food_name:String){
        collectionReference.document(auth.currentUser!!.uid)
            .collection("FavoriteFoods")
            .document(food_name).delete()
    }

    fun cikisYap(a: Activity,targetActivity: Class<*>){
        auth.signOut()
        updateUI(a,targetActivity)
    }

    fun dahaOnceGirisYapildimi(a: Activity,targetActivity: Class<*>){
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(a,targetActivity)
        }
    }

    fun updateUI(a:Activity,targetActivity: Class<*>){
        val intent = Intent(a, targetActivity)
        a.startActivity(intent)
        a.finish()
    }

    fun getLocation(locationTask: Task<Location>, mContext: Context, editText: EditText) {
        locationTask.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(mContext)
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val addressLine = address.getAddressLine(0)
                    editText.setText(addressLine)
                } else {
                    Toast.makeText(mContext,"Adres Bulunamadı",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(mContext,"Konum Bulunamadı",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addOrder(timestamp: Timestamp, order_details:String, order_adress:String, order_total:Int){
        val order = Orders(timestamp,order_details, order_adress, order_total)
        collectionReference.document(auth.currentUser!!.uid)
            .collection("Orders")
            .document(timestamp.toString())
            .set(order)

    }

    fun loadOrders() : MutableLiveData<List<Orders>> {
        collectionReference.document(auth.currentUser!!.uid).collection("Orders").addSnapshotListener { value, error ->
            if(value != null){
                val list = ArrayList<Orders>()
                for(document in value.documents){
                    if(document != null){
                        val order_adress = document.get("order_adress").toString()
                        val order_details = document.get("order_details").toString()
                        val order_time = document.get("order_time")  as? Timestamp
                        val order_total = document.get("order_total").toString()
                        val order = Orders(order_time!!,order_details,order_adress,order_total.toInt())
                        list.add(order)
                    }
                }
                orderList.value = list
            }
        }
        return orderList
    }

    fun loadOrdersBySort(order : String,sortDirection: Query.Direction ): MutableLiveData<List<Orders>> {
        collectionReference.document(auth.currentUser!!.uid)
            .collection("Orders")
            .orderBy(order, sortDirection) // Order by date in descending order
            .addSnapshotListener { value, error ->
                if(value != null){
                    val list = ArrayList<Orders>()
                    for(document in value.documents){
                        if(document != null){
                            val order_adress = document.get("order_adress").toString()
                            val order_details = document.get("order_details").toString()
                            val order_time = document.get("order_time")  as? Timestamp
                            val order_total = document.get("order_total").toString()
                            val order = Orders(order_time!!,order_details,order_adress,order_total.toInt())
                            list.add(order)
                        }
                    }
                    orderList.value = list
                }
            }
        return orderList
    }
}