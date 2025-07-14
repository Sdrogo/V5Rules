package com.example.v5rules.di

import android.content.Context
import android.content.res.Resources
import androidx.credentials.CredentialManager
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.CharacterRepositoryImpl
import com.example.v5rules.repository.FavoriteNpcRepository
import com.example.v5rules.repository.FavoriteNpcRepositoryImpl
import com.example.v5rules.repository.MainRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CharacterRepository {
        return CharacterRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext context: Context): MainRepository {
        return MainRepository(context)
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideFavoriteNpcRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FavoriteNpcRepository {
        return FavoriteNpcRepositoryImpl(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }
}