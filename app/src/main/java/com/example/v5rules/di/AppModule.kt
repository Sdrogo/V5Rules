package com.example.v5rules.di

import android.content.Context
import android.content.res.Resources
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.v5rules.repository.CharacterRepository
import com.example.v5rules.repository.CharacterRepositoryImpl
import com.example.v5rules.repository.MainRepository
import com.example.v5rules.repository.dao.CharacterDao
import com.example.v5rules.repository.db.AppDatabase
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
            .build()

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao = database.characterDao()

    @Provides
    @Singleton
    fun provideCharacterRepository(
        characterDao: CharacterDao,
        mainRepository: MainRepository? = null //Se non ti serve, puoi rimuoverlo.
    ): CharacterRepository =
        CharacterRepositoryImpl(characterDao, mainRepository)

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
}