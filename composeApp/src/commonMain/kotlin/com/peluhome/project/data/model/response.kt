package com.peluhome.project.data.model

import com.peluhome.project.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: RegisterUserData? = null
)

@Serializable
data class RegisterUserData(
    @SerialName("user") val user: User,
    @SerialName("token") val token: String
)

@Serializable
data class SignInResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: SignInData? = null
)

@Serializable
data class SignInData(
    @SerialName("user") val user: User,
    @SerialName("token") val token: String
)

@Serializable
data class ErrorResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("errors") val errors: List<FieldError>? = null
)

@Serializable
data class FieldError(
    @SerialName("field") val field: String,
    @SerialName("message") val message: String,
    @SerialName("value") val value: String? = null
)

@Serializable
data class CategoriesResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: CategoriesData
)

@Serializable
data class CategoriesData(
    @SerialName("categories") val categories: List<com.peluhome.project.domain.model.Category>
)

@Serializable
data class ServicesResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: ServicesData
)

@Serializable
data class ServicesData(
    @SerialName("category") val category: com.peluhome.project.domain.model.Category? = null,
    @SerialName("services") val services: List<com.peluhome.project.domain.model.Service>
)

@Serializable
data class CreateBookingResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: BookingData? = null
)

@Serializable
data class BookingData(
    @SerialName("booking") val booking: com.peluhome.project.domain.model.Booking
)

@Serializable
data class BookingsResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: BookingsData
)

@Serializable
data class BookingsData(
    @SerialName("bookings") val bookings: List<com.peluhome.project.domain.model.BookingWithServices>
)
