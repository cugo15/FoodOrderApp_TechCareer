package com.cugocumhurgunay.yemekkapimda.di

import com.cugocumhurgunay.yemekkapimda.data.datasource.AuthDataSource
import com.cugocumhurgunay.yemekkapimda.data.datasource.FoodsDataSource
import com.cugocumhurgunay.yemekkapimda.data.repo.AuthRepository
import com.cugocumhurgunay.yemekkapimda.data.repo.FoodsRepository
import com.cugocumhurgunay.yemekkapimda.retrofit.ApiUtils
import com.cugocumhurgunay.yemekkapimda.retrofit.FoodsDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(auth: FirebaseAuth,
                              reference: StorageReference,
                              collectionReference: CollectionReference
    ) : AuthDataSource {
        return AuthDataSource(auth,reference,collectionReference)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(ads:AuthDataSource) : AuthRepository {
        return AuthRepository(ads)
    }

    @Provides
    @Singleton
    fun provideAuth() : FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideStorageReference() : StorageReference {
        return Firebase.storage.reference.child("images")
    }

    @Provides
    @Singleton
    fun provideCollectionReference() : CollectionReference {
        //bunu fire store olarak değitirmen gerekecek çünkü iki farklı collections oluturacaksın
        return Firebase.firestore.collection("Users")
    }

    @Provides
    @Singleton
    fun provideFoodsRepository(fds: FoodsDataSource) : FoodsRepository {
        return FoodsRepository(fds)
    }

    @Provides
    @Singleton
    fun provideFoodsDataSource(fdao:FoodsDao , auth: FirebaseAuth) : FoodsDataSource {
        return FoodsDataSource(fdao,auth)
    }

    @Provides
    @Singleton
    fun provideFoodsDao() : FoodsDao {
        return ApiUtils.getFoodsDao()
    }
}