package com.peluhome.project.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StoreManager(
    val store: DataStore<Preferences>
) {
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
}




