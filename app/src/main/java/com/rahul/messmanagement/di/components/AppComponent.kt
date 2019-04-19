package com.rahul.messmanagement.di.components

import com.rahul.messmanagement.data.DataRepository
import com.rahul.messmanagement.di.modules.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DataModule::class)])
interface AppComponent {
    fun getRepository() : DataRepository
}