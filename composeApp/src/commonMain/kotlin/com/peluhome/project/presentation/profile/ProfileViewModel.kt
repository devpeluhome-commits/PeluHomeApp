package com.peluhome.project.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.User
import com.peluhome.project.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
        private set

    fun loadProfile() {
        state = state.copy(isLoading = true, error = null)
        
        CoroutineScope(Dispatchers.Main).launch {
            when (val result = authRepository.getProfile()) {
                is Result.Success -> {
                    state = state.copy(
                        isLoading = false,
                        user = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false,
                        user = null,
                        error = result.message
                    )
                }
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}
