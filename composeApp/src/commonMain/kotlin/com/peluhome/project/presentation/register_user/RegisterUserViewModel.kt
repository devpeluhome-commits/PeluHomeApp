package com.peluhome.project.presentation.register_user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.User
import com.peluhome.project.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterUserViewModel(
    val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegisterUserState())
        private set

    fun registerUser(
        names: String,
        paternalSurname: String,
        maternalSurname: String,
        documentType: String,
        documentNumber: String,
        phone: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val response = withContext(Dispatchers.IO) {
                    authRepository.register(
                        names = names,
                        paternalSurname = paternalSurname,
                        maternalSurname = maternalSurname,
                        documentType = documentType,
                        documentNumber = documentNumber,
                        phone = phone,
                        email = email,
                        password = password
                    )
                }
                when (response) {

                    is Result.Error -> {
                        state = state.copy(error = response.message, isLoading = false, success = null)
                    }

                    is Result.Success -> {
                        state = state.copy(success = response.data, isLoading = false, error = null)
                    }
                }

            } catch (ex: Exception) {
                state = state.copy(error = ex.message, isLoading = false)
            }
        }
    }

    fun clear() {
        state = state.copy(error = null, success = null, isLoading = false)
    }
}

data class RegisterUserState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: User? = null
)




