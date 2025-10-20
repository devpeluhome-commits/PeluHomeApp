package com.peluhome.project.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStorePref(producePath: () -> String): DataStore<Preferences> {

    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            producePath().toPath()
        }
    )

}

internal const val DATA_STORE_FILE_NAME = "peluhome_prefs.preferences_pb"




