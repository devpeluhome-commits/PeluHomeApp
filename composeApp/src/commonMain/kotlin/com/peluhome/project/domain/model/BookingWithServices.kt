package com.peluhome.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingWithServices(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("service_date") val serviceDate: String,
    @SerialName("service_time") val serviceTime: String,
    @SerialName("address") val address: String,
    @SerialName("total_amount") val totalAmount: Double,
    @SerialName("status") val status: String,
    @SerialName("notes") val notes: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("services") val services: List<BookingService>
)

@Serializable
data class BookingService(
    @SerialName("id") val id: Int,
    @SerialName("booking_id") val bookingId: Int,
    @SerialName("service_id") val serviceId: Int,
    @SerialName("quantity") val quantity: Int,
    @SerialName("price") val price: Double,
    @SerialName("service_name") val serviceName: String? = null
)
