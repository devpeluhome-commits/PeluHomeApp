package com.peluhome.project.presentation.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.User
import com.peluhome.project.local.StoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    val authRepository: AuthRepository,
    val storeManager: StoreManager
) : ViewModel() {

    var state by mutableStateOf(SignInState())
        private set

    fun signIn(documentNumber: String, password: String, rememberCredentials: Boolean = false) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val response = withContext(Dispatchers.IO) {
                    authRepository.signIn(
                        documentNumber = documentNumber,
                        password = password
                    )
                }
                when (response) {

                    is Result.Error -> {
                        state = state.copy(error = response.message, isLoading = false, success = null)
                    }

                    is Result.Success -> {
                        val user = response.data
                        val isAdmin = user?.role == "admin"
                        val isClient = user?.role == "client"
                        
                        // Guardar credenciales si el usuario lo solicitó
                        if (rememberCredentials) {
                            storeManager.saveCredentials(documentNumber, password)
                        } else {
                            storeManager.clearCredentials()
                        }
                        
                        state = state.copy(
                            success = user, 
                            isLoading = false, 
                            error = null,
                            isAdmin = isAdmin,
                            isClient = isClient
                        )
                    }
                }

            } catch (ex: Exception) {
                state = state.copy(error = ex.message, isLoading = false)
            }
        }
    }

    fun clear() {
        state = state.copy(error = null, success = null, isLoading = false, isAdmin = false, isClient = false)
    }
    
    fun clearError() {
        state = state.copy(error = null)
    }
    
    // Cargar credenciales guardadas
    suspend fun loadSavedCredentials(): Pair<String?, String?> {
        return storeManager.getSavedCredentials()
    }
    
    // Verificar si recordar credenciales está habilitado
    suspend fun isRememberCredentialsEnabled(): Boolean {
        return storeManager.isRememberCredentialsEnabled()
    }
}

data class SignInState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: User? = null,
    val isAdmin: Boolean = false,
    val isClient: Boolean = false
)




