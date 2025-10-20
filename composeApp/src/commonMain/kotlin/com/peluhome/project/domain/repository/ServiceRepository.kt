package com.peluhome.project.domain.repository

import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.Category
import com.peluhome.project.domain.model.Service

interface ServiceRepository {
    suspend fun getCategories(): Result<List<Category>>
    suspend fun getServicesByCategory(categoryId: Int): Result<List<Service>>
}

