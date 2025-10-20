package com.peluhome.project.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    @SerialName("names") val names: String,
    @SerialName("paternal_surname") val paternalSurname: String,
    @SerialName("maternal_surname") val maternalSurname: String,
    @SerialName("document_type") val documentType: String,
    @SerialName("document_number") val documentNumber: String,
    @SerialName("phone") val phone: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

@Serializable
data class SignInRequest(
    @SerialName("document_number") val documentNumber: String,
    @SerialName("password") val password: String
)




