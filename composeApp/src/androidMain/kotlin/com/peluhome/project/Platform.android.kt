package com.peluhome.project

import android.content.Context
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.peluhome.project.utils.DATA_STORE_FILE_NAME
import com.peluhome.project.utils.createDataStorePref

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return createDataStorePref {
        (context as Context)
            .filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}