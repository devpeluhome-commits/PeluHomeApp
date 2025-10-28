package com.peluhome.project.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.domain.model.BookingWithServices
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_address
import peluhome.composeapp.generated.resources.icon_business_center
import peluhome.composeapp.generated.resources.icon_clock
import peluhome.composeapp.generated.resources.icon_close

@Composable
fun BookingsScreen(
    bookingsViewModel: BookingsViewModel = org.koin.compose.viewmodel.koinViewModel(),
    modifier: Modifier = Modifier,
    availableServices: List<com.peluhome.project.domain.model.Service> = emptyList()
) {
    val state = bookingsViewModel.state

    // Cargar datos cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        bookingsViewModel.loadUserBookings()
    }

    // Manejar errores
    LaunchedEffect(state.error) {
        if (state.error != null) {
            // El error se maneja en el UI directamente
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = ColorPrimary)
            }
        } else if (state.bookings.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_business_center),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No tienes reservas aún",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Solicita tu primer servicio",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(state.bookings) { index, booking ->
                    BookingCard(
                        booking = booking,
                        availableServices = availableServices,
                        reservationNumber = index + 1
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingCard(
    booking: BookingWithServices,
    availableServices: List<com.peluhome.project.domain.model.Service>,
    reservationNumber: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con fecha y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Reserva #$reservationNumber",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = booking.orderNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                
                // Estado
                val (statusText, statusColor) = when (booking.status.lowercase()) {
                    "pending" -> "Pendiente" to Color(0xFFFF9800)
                    "attended" -> "Atendido" to Color(0xFF4CAF50)
                    "confirmed" -> "Confirmado" to Color(0xFF2196F3)
                    "completed" -> "Completado" to Color(0xFF2196F3)
                    "cancelled" -> "Cancelado" to Color(0xFFF44336)
                    else -> booking.status.replaceFirstChar { it.uppercase() } to Color.Gray
                }
                
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = statusColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información de la reserva
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ColorPrimary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Fecha Reserva: ${booking.serviceDate.take(10)} ${booking.serviceTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_address),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = ColorPrimary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = booking.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(12.dp))

            // Servicios
            Text(
                text = "Servicios:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar servicios con nombres del API
            booking.services.forEach { service ->
                // Debug: Log para ver qué datos están llegando
                println("DEBUG BookingsScreen: Service ID: ${service.serviceId}, ServiceName: '${service.serviceName}', CategoryName: '${service.categoryName}'")
                println("DEBUG BookingsScreen: Service completo: $service")
                
                val serviceName = service.serviceName ?: "Servicio #${service.serviceId}"
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$serviceName x${service.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "S/ ${service.price * service.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Mostrar tarifa de delivery (usar datos del API si están disponibles)
            val deliveryCost = booking.deliveryCost ?: com.peluhome.project.utils.DeliveryPricing.calculateDeliveryCost(
                booking.serviceDate, 
                booking.serviceTime
            )
            val deliveryDescription = if (booking.deliveryCost != null) {
                when (booking.deliveryCost) {
                    15.0 -> "Delivery (Domingo/Feriado)"
                    10.0 -> "Delivery (Después de 6 PM)"
                    else -> "Delivery"
                }
            } else {
                com.peluhome.project.utils.DeliveryPricing.getDeliveryDescription(
                    booking.serviceDate, 
                    booking.serviceTime
                )
            }
            
            if (deliveryCost > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = deliveryDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "S/ $deliveryCost",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "S/ ${booking.totalWithDelivery ?: booking.totalAmount}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary
                )
            }

            // Notas si existen
            if (!booking.notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notas: ${booking.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}
