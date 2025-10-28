package com.peluhome.project.di

import androidx.datastore.preferences.core.stringPreferencesKey
import com.peluhome.project.data.installAuthHeaderInterceptor
import com.peluhome.project.data.repository.AuthRepositoryImp
import com.peluhome.project.data.repository.ServiceRepositoryImp
import com.peluhome.project.data.util.Constants
import com.peluhome.project.domain.repository.AuthRepository
import com.peluhome.project.domain.repository.ServiceRepository
import com.peluhome.project.local.StoreManager
import com.peluhome.project.presentation.home.ServicesViewModel
import com.peluhome.project.presentation.profile.ProfileViewModel
import com.peluhome.project.presentation.register_user.RegisterUserViewModel
import com.peluhome.project.presentation.sign_in.SignInViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val dataModule = module {

    single { StoreManager(get()) }

    single {
        val storeManager: StoreManager = get()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }

            installAuthHeaderInterceptor {
                storeManager.getValue(stringPreferencesKey(Constants.PREF_KEY_TOKEN))
            }
        }
    }

    factory<AuthRepository> { AuthRepositoryImp(get(), get()) }
    factory<ServiceRepository> { ServiceRepositoryImp(get()) }
    factory<com.peluhome.project.domain.repository.BookingRepository> { 
        com.peluhome.project.data.repository.BookingRepositoryImp(get()) 
    }
}

val viewModelModule = module {
    viewModel {
        SignInViewModel(authRepository = get())
    }
    viewModel {
        RegisterUserViewModel(authRepository = get())
    }
    viewModel {
        ServicesViewModel(serviceRepository = get(), bookingRepository = get())
    }
    viewModel {
        com.peluhome.project.presentation.history.BookingsViewModel(bookingRepository = get())
    }
    viewModel {
        ProfileViewModel(authRepository = get())
    }
    viewModel {
        com.peluhome.project.presentation.admin.AdminViewModel(bookingRepository = get())
    }
}

expect val dataStoreModule: Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, viewModelModule, dataStoreModule)
    }
}




