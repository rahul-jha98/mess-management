package com.rahul.messmanagement.di.components

import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.di.modules.DataModule
import com.rahul.messmanagement.viewmodels.RoomViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DataModule::class)])
interface AppComponent {
    fun getViewModelFactory() : RoomViewModelFactory
    fun getRepository() : DataRepository
}