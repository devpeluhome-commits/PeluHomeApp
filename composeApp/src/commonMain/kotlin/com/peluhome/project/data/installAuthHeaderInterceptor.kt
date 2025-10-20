package com.peluhome.project.data

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking

fun HttpClientConfig<*>.installAuthHeaderInterceptor(getToken: suspend () -> String?) {
    install(DefaultRequest) {
        val token = runBlocking { getToken() }
        if (!token.isNullOrBlank()) {
            headers.append(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}




