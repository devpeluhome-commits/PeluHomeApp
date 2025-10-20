package com.peluhome.project.presentation.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.BookingWithServices
import com.peluhome.project.domain.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingsViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    var state by mutableStateOf(BookingsState())
        private set

    init {
        loadUserBookings()
    }

    fun loadUserBookings() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val result = withContext(Dispatchers.IO) {
                bookingRepository.getUserBookings()
            }
            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        bookings = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshBookings() {
        loadUserBookings()
    }

    fun clearError() {
        state = state.copy(error = null)
    }
}

data class BookingsState(
    val isLoading: Boolean = false,
    val bookings: List<BookingWithServices> = emptyList(),
    val error: String? = null
)
