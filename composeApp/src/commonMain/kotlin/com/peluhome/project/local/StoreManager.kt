package com.peluhome.project.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StoreManager(
    val store: DataStore<Preferences>
) {
    // Keys para credenciales
    companion object {
        val REMEMBER_CREDENTIALS = stringPreferencesKey("remember_credentials")
        val SAVED_DOCUMENT_NUMBER = stringPreferencesKey("saved_document_number")
        val SAVED_PASSWORD = stringPreferencesKey("saved_password")
    }

    suspend inline fun <reified T> saveValue(key: Preferences.Key<String>, value: T) {
        val jsonValue = Json.encodeToString(value)
        store.edit {
            it[key] = jsonValue
        }
    }

    suspend inline fun <reified T> getValue(key: Preferences.Key<String>): T? {
        val jsonValue = store.data.map {
            it[key] ?: return@map null
        }.first()
        return jsonValue?.let { Json.decodeFromString(it) }
    }

    suspend fun removeValue(key: Preferences.Key<String>) {
        store.edit {
            it.remove(key)
        }
    }
    
    // Métodos específicos para credenciales
    suspend fun saveCredentials(documentNumber: String, password: String) {
        store.edit {
            it[SAVED_DOCUMENT_NUMBER] = documentNumber
            it[SAVED_PASSWORD] = password
            it[REMEMBER_CREDENTIALS] = "true"
        }
    }
    
    suspend fun getSavedCredentials(): Pair<String?, String?> {
        val prefs = store.data.first()
        val documentNumber = prefs[SAVED_DOCUMENT_NUMBER]
        val password = prefs[SAVED_PASSWORD]
        return Pair(documentNumber, password)
    }
    
    suspend fun clearCredentials() {
        store.edit {
            it.remove(SAVED_DOCUMENT_NUMBER)
            it.remove(SAVED_PASSWORD)
            it.remove(REMEMBER_CREDENTIALS)
        }
    }
    
    suspend fun isRememberCredentialsEnabled(): Boolean {
        val prefs = store.data.first()
        return prefs[REMEMBER_CREDENTIALS] == "true"
    }
}




