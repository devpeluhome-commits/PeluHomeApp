package com.peluhome.project.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.Booking
import com.peluhome.project.domain.model.Category
import com.peluhome.project.domain.model.Service
import com.peluhome.project.domain.repository.BookingRepository
import com.peluhome.project.domain.repository.ServiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServicesViewModel(
    private val serviceRepository: ServiceRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    var state by mutableStateOf(ServicesState())
        private set

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            state = state.copy(isLoadingCategories = true, error = null)
            val result = withContext(Dispatchers.IO) {
                serviceRepository.getCategories()
            }
            when (result) {
                is Result.Success -> {
                    state = state.copy(categories = result.data ?: emptyList(), isLoadingCategories = false)
                }
                is Result.Error -> {
                    state = state.copy(error = result.message, isLoadingCategories = false)
                }
            }
        }
    }

    fun loadServicesByCategory(categoryId: Int) {
        viewModelScope.launch {
            state = state.copy(isLoadingServices = true, error = null)
            val result = withContext(Dispatchers.IO) {
                serviceRepository.getServicesByCategory(categoryId)
            }
            when (result) {
                is Result.Success -> {
                    state = state.copy(services = result.data ?: emptyList(), isLoadingServices = false)
                }
                is Result.Error -> {
                    state = state.copy(error = result.message, isLoadingServices = false)
                }
            }
        }
    }

    fun createBooking(
        serviceDate: String,
        serviceTime: String,
        address: String,
        services: List<Pair<Int, Int>>, // Pair<serviceId, quantity>
        servicesPrices: Map<Int, Double>,
        notes: String?
    ) {
        viewModelScope.launch {
            state = state.copy(isCreatingBooking = true, error = null, bookingCreated = null)
            val result = withContext(Dispatchers.IO) {
                bookingRepository.createBooking(
                    serviceDate = serviceDate,
                    serviceTime = serviceTime,
                    address = address,
                    services = services,
                    servicesPrices = servicesPrices,
                    notes = notes
                )
            }
            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        bookingCreated = result.data,
                        isCreatingBooking = false
                    )
                }
                is Result.Error -> {
                    println("DEBUG ServicesViewModel: Error al crear booking: ${result.message}")
                    state = state.copy(
                        error = result.message,
                        isCreatingBooking = false
                    )
                }
            }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }

    fun clearBookingCreated() {
        state = state.copy(bookingCreated = null)
    }
}

data class ServicesState(
    val isLoadingCategories: Boolean = false,
    val isLoadingServices: Boolean = false,
    val isCreatingBooking: Boolean = false,
    val categories: List<Category> = emptyList(),
    val services: List<Service> = emptyList(),
    val bookingCreated: Booking? = null,
    val error: String? = null
)

