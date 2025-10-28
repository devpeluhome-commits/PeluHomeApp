package com.peluhome.project.presentation.admin

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

data class AdminState(
    val isLoading: Boolean = false,
    val bookings: List<BookingWithServices> = emptyList(),
    val error: String? = null
)

class AdminViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {
    var state by mutableStateOf(AdminState())
        private set

    fun loadAllBookings() {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                bookingRepository.getAllBookings()
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

    fun updateBookingStatus(bookingId: Int, newStatus: String) {
        state = state.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                bookingRepository.updateBookingStatus(bookingId, newStatus)
            }
            when (result) {
                is Result.Success -> {
                    // Recargar la lista de reservas
                    loadAllBookings()
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

    fun clearError() {
        state = state.copy(error = null)
    }
}
