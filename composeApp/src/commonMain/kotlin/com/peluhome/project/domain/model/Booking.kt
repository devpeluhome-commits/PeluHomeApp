package com.peluhome.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    @SerialName("id") val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("service_date") val serviceDate: String,
    @SerialName("service_time") val serviceTime: String,
    @SerialName("address") val address: String,
    @SerialName("total_amount") val totalAmount: Double,
    @SerialName("status") val status: String,
    @SerialName("notes") val notes: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String
)

