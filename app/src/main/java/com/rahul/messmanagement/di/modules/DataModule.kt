package com.rahul.messmanagement.di.modules

import android.content.Context
import com.rahul.messmanagement.AppExecutors
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.network.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(NetworkModule::class)])
object DataModule {
    @Provides
    @Singleton
    fun provideAppExecutors() = AppExecutors.getInstance()



    @Provides
    @Singleton
    fun provideDataRepository(retrofitClient : ApiService, appExecutors: AppExecutors) =
        DataRepository.getInstance(retrofitClient,
            appExecutors)
}