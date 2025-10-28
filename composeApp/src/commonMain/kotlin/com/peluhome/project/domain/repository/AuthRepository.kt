package com.peluhome.project.domain.repository

import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.User

interface AuthRepository {

    suspend fun signIn(documentNumber: String, password: String): Result<User>
    
    suspend fun register(
        names: String,
        paternalSurname: String,
        maternalSurname: String,
        documentType: String,
        documentNumber: String,
        phone: String,
        email: String,
        password: String
    ): Result<User>
    
    suspend fun getUser(): Result<User?>
    
    suspend fun getProfile(): Result<User>
    
    suspend fun logout(): Result<Boolean>
}




