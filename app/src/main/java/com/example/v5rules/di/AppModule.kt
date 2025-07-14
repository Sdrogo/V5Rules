package com.example.v5rules.di

import android.content.Context
import android.content.res.Resources
import androidx.credentials.CredentialManager
import androidx.room.Room
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.CharacterRepositoryImpl
import com.example.v5rules.repository.FavoriteNpcRepository
import com.example.v5rules.repository.FavoriteNpcRepositoryImpl
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.repository.dao.CharacterDao
import com.example.v5rules.repository.db.AppDatabase
import com.example.v5rules.repository.db.AppDatabase.Companion.MIGRATION_3_4
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            // Note: If you remove FavoriteNpc from the database entities,
            // you might need to adjust or remove this migration.
            .addMigrations(MIGRATION_3_4)
            .build()

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao = database.characterDao()

    @Provides
    @Singleton
    fun provideCharacterRepository(
        characterDao: CharacterDao
    ): CharacterRepository =
        CharacterRepositoryImpl(characterDao)

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

    // REMOVED: No longer needed as we use Firestore for favorites
    // @Provides
    // @Singleton
    // fun provideFavoriteNpcDao(appDatabase: AppDatabase): FavoriteNpcDao {
    //     return appDatabase.favoriteNpcDao()
    // }

    @Provides
    @Singleton
    fun provideFavoriteNpcRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): FavoriteNpcRepository {
        // Now providing the Firestore-based implementation
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