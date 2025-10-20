package com.peluhome.project.data.repository

import com.peluhome.project.core.Result
import com.peluhome.project.data.model.BookingServiceItem
import com.peluhome.project.data.model.BookingsResponse
import com.peluhome.project.data.model.CreateBookingRequest
import com.peluhome.project.data.model.CreateBookingResponse
import com.peluhome.project.data.model.ErrorResponse
import com.peluhome.project.data.util.Constants
import com.peluhome.project.domain.model.Booking
import com.peluhome.project.domain.model.BookingWithServices
import com.peluhome.project.domain.repository.BookingRepository
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

class BookingRepositoryImp(
    private val httpClient: HttpClient
) : BookingRepository {

    override suspend fun createBooking(
        serviceDate: String,
        serviceTime: String,
        address: String,
        services: List<Pair<Int, Int>>,
        servicesPrices: Map<Int, Double>,
        notes: String?
    ): Result<Booking> {
        try {
            // Convertir los servicios al formato esperado por el API
            val bookingServices = services.map { (serviceId, quantity) ->
                BookingServiceItem(
                    serviceId = serviceId,
                    quantity = quantity,
                    price = servicesPrices[serviceId] ?: 0.0
                )
            }

            val requestBody = CreateBookingRequest(
                serviceDate = serviceDate,
                serviceTime = serviceTime,
                address = address,
                services = bookingServices,
                notes = notes
            )

            val jsonRequest = Json { prettyPrint = true }.encodeToString(
                CreateBookingRequest.serializer(),
                requestBody
            )
            println("========================================")
            println("📤 CREAR RESERVA - REQUEST JSON:")
            println("URL: ${Constants.URL_BASE}${Constants.CREATE_BOOKING}")
            println(jsonRequest)
            println("========================================")

            val response = httpClient.post("${Constants.URL_BASE}${Constants.CREATE_BOOKING}") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            println("📥 CREAR RESERVA - RESPONSE STATUS: ${response.status}")

            when (response.status) {
                HttpStatusCode.Created -> {
                    val resp: CreateBookingResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(
                        CreateBookingResponse.serializer(),
                        resp
                    )
                    println("✅ CREAR RESERVA - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")

                    if (resp.success && resp.data != null) {
                        return Result.Success(data = resp.data.booking)
                    } else {
                        return Result.Error(message = resp.message)
                    }
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ CREAR RESERVA - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")

                    try {
                        val errorResp = Json { ignoreUnknownKeys = true }
                            .decodeFromString<ErrorResponse>(errorJson)
                        
                        // Si hay errores específicos de campos, usar el primer error específico
                        val errorMessage = if (!errorResp.errors.isNullOrEmpty()) {
                            errorResp.errors.first().message
                        } else {
                            errorResp.message
                        }
                        
                        return Result.Error(message = errorMessage)
                    } catch (e: Exception) {
                        return Result.Error(message = "Error del servidor: ${response.status}")
                    }
                }
            }
        } catch (ex: IOException) {
            println("❌ ERROR DE CONEXIÓN AL CREAR RESERVA: ${ex.message}")
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            println("❌ ERROR AL CREAR RESERVA: ${ex.message}")
            ex.printStackTrace()
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }

    override suspend fun getUserBookings(): Result<List<BookingWithServices>> {
        try {
            println("========================================")
            println("📤 OBTENER RESERVAS DEL USUARIO")
            println("URL: ${Constants.URL_BASE}${Constants.GET_USER_BOOKINGS}")
            println("========================================")

            val response = httpClient.get("${Constants.URL_BASE}${Constants.GET_USER_BOOKINGS}")

            println("📥 RESERVAS - RESPONSE STATUS: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val resp: BookingsResponse = response.body()
                    val jsonResponse = Json { prettyPrint = true }.encodeToString(
                        BookingsResponse.serializer(),
                        resp
                    )
                    println("✅ RESERVAS - RESPONSE JSON (SUCCESS):")
                    println(jsonResponse)
                    println("========================================")

                    if (resp.success) {
                        return Result.Success(data = resp.data.bookings)
                    } else {
                        return Result.Error(message = "Error al obtener reservas")
                    }
                }

                else -> {
                    val errorJson = response.bodyAsText()
                    println("❌ RESERVAS - RESPONSE JSON (ERROR ${response.status}):")
                    println(errorJson)
                    println("========================================")

                    try {
                        val errorResp = Json { ignoreUnknownKeys = true }
                            .decodeFromString<ErrorResponse>(errorJson)
                        
                        // Si hay errores específicos de campos, usar el primer error específico
                        val errorMessage = if (!errorResp.errors.isNullOrEmpty()) {
                            errorResp.errors.first().message
                        } else {
                            errorResp.message
                        }
                        
                        return Result.Error(message = errorMessage)
                    } catch (e: Exception) {
                        return Result.Error(message = "Error del servidor: ${response.status}")
                    }
                }
            }
        } catch (ex: IOException) {
            println("❌ ERROR DE CONEXIÓN AL OBTENER RESERVAS: ${ex.message}")
            return Result.Error(message = "Compruebe su conexión a internet")
        } catch (ex: Exception) {
            println("❌ ERROR AL OBTENER RESERVAS: ${ex.message}")
            ex.printStackTrace()
            return Result.Error(message = ex.message ?: "Error desconocido")
        }
    }
}

