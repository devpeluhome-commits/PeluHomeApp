package com.peluhome.project

import android.app.Application
import com.peluhome.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class PeluHomeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@PeluHomeApplication)
        }
    }
}




