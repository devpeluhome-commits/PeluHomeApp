package com.peluhome.project.data.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import com.peluhome.project.core.Result
import com.peluhome.project.data.model.ErrorResponse
import com.peluhome.project.data.model.ProfileResponse
import com.peluhome.project.data.model.RegisterUserRequest
import com.peluhome.project.data.model.RegisterUserResponse
import com.peluhome.project.data.model.SignInRequest
import com.peluhome.project.data.model.SignInResponse
import com.peluhome.project.data.util.Constants
import com.peluhome.project.domain.model.User
import com.peluhome.project.domain.repository.AuthRepository
import com.peluhome.project.local.StoreManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json

class AuthRepositoryImp(
    val httpClient: HttpClient,
    val storeManager: StoreManager
) : AuthRepository {

    override suspend fun signIn(
        documentNumber: String,
        password: String
    ): Result<User> {
        try {
            val requestBody = SignInRequest(
                documentNumber = documentNumber,
                password = password
            )

            
            val response = httpClient.post("${Constants.URL_BASE}${Constants.SIGN_IN}") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }


            when (response.status) {
                HttpStatusCode.OK -> {
                    val resp: SignInResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(SignInResponse.serializer(), resp)
                    println("✅ LOGIN - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")
                    
                    if (resp.success && resp.data != null) {
                        // Guardar usuario y token en preferencias
                        val keyUser = stringPreferencesKey(Constants.PREF_KEY_USER)
                        storeManager.saveValue(keyUser, resp.data.user)
                        val keyToken = stringPreferencesKey(Constants.PREF_KEY_TOKEN)
                        storeManager.saveValue(keyToken, resp.data.token)
                        return Result.Success(data = resp.data.user)
                    } else {
                        return Result.Error(message = resp.message)
                    }
                }

                HttpStatusCode.BadRequest -> {
                    val errorJson = response.bodyAsText()
                    println("❌ LOGIN - RESPONSE JSON (BAD REQUEST):")
                    println(errorJson)
                    println("========================================")
                    val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                    return Result.Error(message = errorResp.message)
                }

                HttpStatusCode.Unauthorized -> {
                    val errorJson = response.bodyAsText()
                    println("❌ LOGIN - RESPONSE JSON (UNAUTHORIZED):")
                    println(errorJson)
                    println("========================================")
                    val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                    return Result.Error(message = errorResp.message)
                }

                HttpStatusCode.NotFound -> {
                    val errorJson = response.bodyAsText()
                    println("❌ LOGIN - RESPONSE JSON (NOT FOUND):")
                    println(errorJson)
                    println("========================================")
                    val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                    return Result.Error(message = errorResp.message)
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ LOGIN - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")
                    return Result.Error(message = "Error del servidor: ${response.status}")
                }
            }
        } catch (ex: IOException) {
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun register(
        names: String,
        paternalSurname: String,
        maternalSurname: String,
        documentType: String,
        documentNumber: String,
        phone: String,
        email: String,
        password: String
    ): Result<User> {
        try {
            val requestBody = RegisterUserRequest(
                names = names,
                paternalSurname = paternalSurname,
                maternalSurname = maternalSurname,
                documentType = documentType,
                documentNumber = documentNumber,
                phone = phone,
                email = email,
                password = password
            )


            val response = httpClient.post("${Constants.URL_BASE}${Constants.REGISTER_USER}") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            when (response.status) {
                HttpStatusCode.Created -> {
                    val resp: RegisterUserResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(RegisterUserResponse.serializer(), resp)
                    println("✅ REGISTRO - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")
                    
                    if (resp.success && resp.data != null) {
                        // Guardar usuario y token en preferencias
                        val keyUser = stringPreferencesKey(Constants.PREF_KEY_USER)
                        storeManager.saveValue(keyUser, resp.data.user)
                        val keyToken = stringPreferencesKey(Constants.PREF_KEY_TOKEN)
                        storeManager.saveValue(keyToken, resp.data.token)
                        return Result.Success(data = resp.data.user)
                    } else {
                        return Result.Error(message = resp.message)
                    }
                }

                HttpStatusCode.BadRequest -> {
                    val errorJson = response.bodyAsText()
                    println("❌ REGISTRO - RESPONSE JSON (BAD REQUEST):")
                    println(errorJson)
                    println("========================================")
                    val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                    return Result.Error(message = errorResp.message)
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ REGISTRO - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")
                    return Result.Error(message = "Error del servidor: ${response.status}")
                }
            }
        } catch (ex: IOException) {
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun getUser(): Result<User?> {
        try {
            val key = stringPreferencesKey(Constants.PREF_KEY_USER)
            val user: User? = storeManager.getValue(key)
            return Result.Success(data = user)
        } catch (ex: IOException) {
            return Result.Error(message = "Error al obtener usuario")
        } catch (ex: Exception) {
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun getProfile(): Result<User> {
        try {
            val response = httpClient.get("${Constants.URL_BASE}${Constants.GET_PROFILE}") {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val resp: ProfileResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(ProfileResponse.serializer(), resp)
                    println("✅ PROFILE - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")
                    
                    if (resp.success && resp.data != null) {
                        // Actualizar usuario en preferencias
                        val keyUser = stringPreferencesKey(Constants.PREF_KEY_USER)
                        storeManager.saveValue(keyUser, resp.data.user)
                        return Result.Success(data = resp.data.user)
                    } else {
                        return Result.Error(message = "Error al obtener perfil")
                    }
                }

                HttpStatusCode.Unauthorized -> {
                    val errorJson = response.bodyAsText()
                    println("❌ PROFILE - RESPONSE JSON (UNAUTHORIZED):")
                    println(errorJson)
                    println("========================================")
                    val errorResp = Json { ignoreUnknownKeys = true }.decodeFromString<ErrorResponse>(errorJson)
                    return Result.Error(message = errorResp.message)
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ PROFILE - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")
                    return Result.Error(message = "Error del servidor: ${response.status}")
                }
            }
        } catch (ex: IOException) {
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun logout(): Result<Boolean> {
        try {
            val keyUser = stringPreferencesKey(Constants.PREF_KEY_USER)
            val keyToken = stringPreferencesKey(Constants.PREF_KEY_TOKEN)
            storeManager.removeValue(keyUser)
            storeManager.removeValue(keyToken)
            return Result.Success(data = true)
        } catch (ex: Exception) {
            return Result.Error(message = ex.message ?: "Error al cerrar sesión")
        }
    }
}



