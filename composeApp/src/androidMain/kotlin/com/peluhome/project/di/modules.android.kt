package com.peluhome.project.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.peluhome.project.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataStoreModule = module {
    single<DataStore<Preferences>> {
        createDataStore(context = androidContext())
    }
}




