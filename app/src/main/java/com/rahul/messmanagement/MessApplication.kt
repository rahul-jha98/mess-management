package com.rahul.messmanagement

import android.app.Application
import com.rahul.messmanagement.di.components.AppComponent
import com.rahul.messmanagement.di.components.DaggerAppComponent
import com.rahul.messmanagement.di.modules.ContextModule
import com.rahul.messmanagement.di.modules.DataModule
import com.rahul.messmanagement.di.modules.NetworkModule

class MessApplication : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .networkModule(NetworkModule)
            .dataModule(DataModule)
            .build()
    }
}