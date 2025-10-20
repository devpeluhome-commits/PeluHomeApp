package com.peluhome.project.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    @SerialName("id") val id: Int,
    @SerialName("category_id") val categoryId: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("price") val price: Double,
    @SerialName("duration_minutes") val durationMinutes: Int
)

