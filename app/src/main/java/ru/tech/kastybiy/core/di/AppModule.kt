package ru.tech.kastybiy.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.tech.kastybiy.core.constants.Constants.BASE_URL
import ru.tech.kastybiy.data.local.KastybiyDatabase
import ru.tech.kastybiy.data.remote.api.KastybiyApi
import ru.tech.kastybiy.data.repository.KastybiyRepositoryImpl
import ru.tech.kastybiy.domain.repository.KastybiyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideKastybiyApi(): KastybiyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(KastybiyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ): KastybiyDatabase = Room.databaseBuilder(
        applicationContext,
        KastybiyDatabase::class.java,
        "kastybiy_db"
    ).build()

    @Provides
    @Singleton
    fun provideKastybiyRepository(api: KastybiyApi, db: KastybiyDatabase): KastybiyRepository =
        KastybiyRepositoryImpl(api, db.favRecipeDao, db.fridgeDao)

}