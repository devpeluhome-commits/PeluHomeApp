package com.peluhome.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingWithServices(
    @SerialName("id") val id: Int,
    @SerialName("order_number") val orderNumber: String,
    @SerialName("user_id") val userId: Int,
    @SerialName("service_date") val serviceDate: String,
    @SerialName("service_time") val serviceTime: String,
    @SerialName("address") val address: String,
    @SerialName("total_amount") val totalAmount: Double,
    @SerialName("delivery_cost") val deliveryCost: Double? = null,
    @SerialName("total_with_delivery") val totalWithDelivery: Double? = null,
    @SerialName("status") val status: String,
    @SerialName("notes") val notes: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("services") val services: List<BookingService>,
    // Datos del usuario
    @SerialName("names") val userNames: String? = null,
    @SerialName("paternal_surname") val userPaternalSurname: String? = null,
    @SerialName("maternal_surname") val userMaternalSurname: String? = null,
    @SerialName("document_type") val userDocumentType: String? = null,
    @SerialName("document_number") val userDocumentNumber: String? = null,
    @SerialName("phone") val userPhone: String? = null,
    @SerialName("email") val userEmail: String? = null
) {
    // Función helper para obtener el nombre completo del usuario
    fun getUserFullName(): String {
        val names = userNames ?: ""
        val paternalSurname = userPaternalSurname ?: ""
        val maternalSurname = userMaternalSurname ?: ""
        
        return listOf(names, paternalSurname, maternalSurname)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { "Usuario no disponible" }
    }
}

@Serializable
data class BookingService(
    @SerialName("id") val id: Int,
    @SerialName("booking_id") val bookingId: Int,
    @SerialName("service_id") val serviceId: Int,
    @SerialName("quantity") val quantity: Int,
    @SerialName("price") val price: Double,
    @SerialName("service_name") val serviceName: String? = null,
    @SerialName("category_name") val categoryName: String? = null,
    @SerialName("duration_minutes") val durationMinutes: Int? = null,
    @SerialName("subtotal") val subtotal: Double? = null,
    @SerialName("created_at") val createdAt: String? = null
)
