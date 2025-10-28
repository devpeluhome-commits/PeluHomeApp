package com.peluhome.project.domain.repository

import com.peluhome.project.core.Result
import com.peluhome.project.domain.model.Booking
import com.peluhome.project.domain.model.BookingWithServices

interface BookingRepository {
    suspend fun createBooking(
        serviceDate: String,
        serviceTime: String,
        address: String,
        services: List<Pair<Int, Int>>, // Pair<serviceId, quantity>
        servicesPrices: Map<Int, Double>, // Map<serviceId, price>
        notes: String?
    ): Result<Booking>
    
    suspend fun getUserBookings(): Result<List<BookingWithServices>>
    
    // Métodos de admin
    suspend fun getAllBookings(): Result<List<BookingWithServices>>
    suspend fun updateBookingStatus(bookingId: Int, status: String): Result<Boolean>
}

