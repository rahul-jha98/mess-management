package com.rahul.messmanagement.di.modules

import android.content.Context
import com.rahul.messmanagement.AppExecutors
import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.data.database.MessDatabase
import com.rahul.messmanagement.data.network.ApiService
import com.rahul.messmanagement.viewmodels.RoomViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(NetworkModule::class), (ContextModule::class)])
object DataModule {
    @Provides
    @Singleton
    fun provideAppExecutors() = AppExecutors.getInstance()

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) = MessDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideDataRepository(retrofitClient : ApiService, messDatabase: MessDatabase, appExecutors: AppExecutors) =
        DataRepository.getInstance(retrofitClient,
            messDatabase.attendanceDao(),
            appExecutors)

    @Provides
    @Singleton
    fun provideViewModelFactory(repository : DataRepository) = RoomViewModelFactory(repository)
}