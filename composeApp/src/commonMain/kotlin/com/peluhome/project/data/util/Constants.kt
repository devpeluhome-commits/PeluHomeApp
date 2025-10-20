package com.peluhome.project.data.util

class Constants {

    companion object {

        // URL Base del API - Cambiar según el entorno
        const val URL_BASE = "https://peluhomeapi-production.up.railway.app/api/"
        
        // Endpoints de autenticación
        const val REGISTER_USER = "auth/register"
        const val SIGN_IN = "auth/login"
        const val GET_PROFILE = "auth/profile"
        
        // Endpoints de servicios
        const val GET_CATEGORIES = "services/categories"
        const val GET_SERVICES = "services/categories"
        
        // Claves de preferencias locales
        const val PREF_KEY_TOKEN = "PreferencesStoreToken"
        const val PREF_KEY_USER = "PreferencesStoreUser"
    }
}




