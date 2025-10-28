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
        const val GET_SERVICES = "services/categories" // Ruta base para servicios por categoría
        
        // Endpoints de reservas
        const val CREATE_BOOKING = "bookings"
        const val GET_USER_BOOKINGS = "bookings"
        
        // Endpoints de admin
        const val ADMIN_GET_ALL_BOOKINGS = "admin/bookings"
        const val ADMIN_UPDATE_BOOKING_STATUS = "admin/bookings"
        
        // Claves de preferencias locales
        const val PREF_KEY_TOKEN = "PreferencesStoreToken"
        const val PREF_KEY_USER = "PreferencesStoreUser"
    }
}




