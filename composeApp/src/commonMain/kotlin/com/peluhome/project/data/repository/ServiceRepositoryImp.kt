package com.peluhome.project.data.repository

import com.peluhome.project.core.Result
import com.peluhome.project.data.model.CategoriesResponse
import com.peluhome.project.data.model.ErrorResponse
import com.peluhome.project.data.model.ServicesResponse
import com.peluhome.project.data.util.Constants
import com.peluhome.project.domain.model.Category
import com.peluhome.project.domain.model.Service
import com.peluhome.project.domain.repository.ServiceRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json

class ServiceRepositoryImp(
    val httpClient: HttpClient
) : ServiceRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        try {
            println("========================================")
            println("📤 OBTENER CATEGORÍAS")
            println("URL: ${Constants.URL_BASE}${Constants.GET_CATEGORIES}")
            println("========================================")
            
            val response = httpClient.get("${Constants.URL_BASE}${Constants.GET_CATEGORIES}")
            
            println("📥 CATEGORÍAS - RESPONSE STATUS: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val resp: CategoriesResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(CategoriesResponse.serializer(), resp)
                    println("✅ CATEGORÍAS - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")
                    
                    if (resp.success) {
                        return Result.Success(data = resp.data.categories)
                    } else {
                        return Result.Error(message = "Error al obtener categorías")
                    }
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ CATEGORÍAS - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")
                    
                    try {
                        val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                        return Result.Error(message = errorResp.message)
                    } catch (e: Exception) {
                        return Result.Error(message = "Error del servidor: ${response.status}")
                    }
                }
            }
        } catch (ex: IOException) {
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            println("❌ ERROR AL OBTENER CATEGORÍAS: ${ex.message}")
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun getServicesByCategory(categoryId: Int): Result<List<Service>> {
        try {
            println("========================================")
            println("📤 OBTENER SERVICIOS POR CATEGORÍA")
            println("URL: ${Constants.URL_BASE}${Constants.GET_SERVICES}/$categoryId/services")
            println("========================================")
            
            val response = httpClient.get("${Constants.URL_BASE}${Constants.GET_SERVICES}/$categoryId/services")
            
            println("📥 SERVICIOS - RESPONSE STATUS: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val resp: ServicesResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(ServicesResponse.serializer(), resp)
                    println("✅ SERVICIOS - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")
                    
                    if (resp.success) {
                        return Result.Success(data = resp.data.services)
                    } else {
                        return Result.Error(message = "Error al obtener servicios")
                    }
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ SERVICIOS - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")
                    
                    try {
                        val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                        return Result.Error(message = errorResp.message)
                    } catch (e: Exception) {
                        return Result.Error(message = "Error del servidor: ${response.status}")
                    }
                }
            }
        } catch (ex: IOException) {
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            println("❌ ERROR AL OBTENER SERVICIOS: ${ex.message}")
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }
}

