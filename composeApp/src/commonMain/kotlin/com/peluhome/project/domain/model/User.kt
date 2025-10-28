package com.peluhome.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("names") val names: String,
    @SerialName("paternal_surname") val paternalSurname: String,
    @SerialName("maternal_surname") val maternalSurname: String,
    @SerialName("document_type") val documentType: String,
    @SerialName("document_number") val documentNumber: String,
    @SerialName("phone") val phone: String,
    @SerialName("email") val email: String,
    @SerialName("role") val role: String = "client",
    @SerialName("created_at") val createdAt: String? = null
)




