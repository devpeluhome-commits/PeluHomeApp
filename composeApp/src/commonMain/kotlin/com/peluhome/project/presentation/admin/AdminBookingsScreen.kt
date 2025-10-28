package com.peluhome.project.presentation.admin

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peluhome.project.domain.model.BookingWithServices
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.presentation.components.ConfirmDialog
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_business_center
import peluhome.composeapp.generated.resources.icon_sign_out
import peluhome.composeapp.generated.resources.icon_sync
import peluhome.composeapp.generated.resources.logo_peluhome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminBookingsScreen(
    adminViewModel: AdminViewModel = org.koin.compose.viewmodel.koinViewModel(),
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state = adminViewModel.state

    // Estados para los diálogos
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedBookingId by remember { mutableStateOf(0) }
    var selectedAction by remember { mutableStateOf("") }
    
    // Estado para el buscador
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Cargar datos cada vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        adminViewModel.loadAllBookings()
    }
    
    // Filtrar reservas por número correlativo
    val filteredBookings = remember(state.bookings, searchQuery) {
        if (searchQuery.isBlank()) {
            state.bookings
        } else {
            state.bookings.filter { booking ->
                // Extraer los últimos 4 dígitos del order_number
                val orderNumber = booking.orderNumber
                val lastFourDigits = orderNumber.takeLast(4)
                lastFourDigits == searchQuery
            }
        }
    }

    // Manejar errores
    LaunchedEffect(state.error) {
        if (state.error != null) {
            // El error se maneja en el UI directamente
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo_peluhome),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(32.dp),
                            contentScale = ContentScale.FillHeight,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPrimary,
                    titleContentColor = ColorPrimary
                ),
                actions = {
                    // Botón de refrescar
                    IconButton(
                        onClick = { adminViewModel.loadAllBookings() }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_sync),
                            contentDescription = "Refrescar",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    // Botón de cerrar sesión
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_sign_out),
                            contentDescription = "Cerrar Sesión",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.background(Color.White.copy(alpha = 0.95f))
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = ColorPrimary
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Campo de búsqueda
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar por número correlativo (ej: 0001)") },
                        placeholder = { Text("") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { focusManager.clearFocus() }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorPrimary,
                            unfocusedBorderColor = Color.Gray
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                    )
                    
                    if (state.bookings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
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
                                    text = "No hay reservas",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Las reservas aparecerán aquí cuando los clientes las creen",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else if (filteredBookings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
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
                                    text = "No se encontraron reservas",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "No hay reservas con el número correlativo '$searchQuery'",
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
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredBookings) { booking ->
                                AdminBookingCard(
                                    booking = booking,
                                    onStatusChange = { bookingId, newStatus ->
                                        selectedBookingId = bookingId
                                        selectedAction = newStatus
                                        when (newStatus) {
                                            "attended" -> showConfirmDialog = true
                                            "cancelled" -> showCancelDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Mostrar error si existe
            state.error?.let { error ->
                AlertComponent(
                    title = "Error",
                    message = error,
                    dismiss = { adminViewModel.clearError() },
                    action = { adminViewModel.clearError() }
                )
            }
            
            // Diálogo de confirmación de logout
            if (showLogoutDialog) {
                AlertComponent(
                    title = "Cerrar Sesión",
                    message = "¿Estás seguro de que quieres cerrar sesión?",
                    dismiss = { showLogoutDialog = false },
                    action = {
                        showLogoutDialog = false
                        onLogout()
                    }
                )
            }
            
            // Diálogo de confirmación para confirmar reserva
            if (showConfirmDialog) {
                ConfirmDialog(
                    title = "Confirmar Reserva",
                    message = "¿Estás seguro de que quieres confirmar esta reserva?",
                    onCancel = { showConfirmDialog = false },
                    onConfirm = {
                        showConfirmDialog = false
                        adminViewModel.updateBookingStatus(selectedBookingId, selectedAction)
                    }
                )
            }
            
            // Diálogo de confirmación para cancelar reserva
            if (showCancelDialog) {
                ConfirmDialog(
                    title = "Cancelar Reserva",
                    message = "¿Estás seguro de que quieres cancelar esta reserva?",
                    onCancel = { showCancelDialog = false },
                    onConfirm = {
                        showCancelDialog = false
                        adminViewModel.updateBookingStatus(selectedBookingId, selectedAction)
                    }
                )
            }
        }
    }
}

@Composable
private fun AdminBookingCard(
    booking: BookingWithServices,
    onStatusChange: (Int, String) -> Unit,
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
                // Header con información básica
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = booking.orderNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "Fecha Reserva: ${booking.serviceDate.take(10)} ${booking.serviceTime}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    // Estado actual
                    val (statusText, statusColor) = when (booking.status.lowercase()) {
                        "pending" -> "Pendiente" to Color(0xFFFF9800)
                        "attended" -> "Atendido" to Color(0xFF4CAF50)
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
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(12.dp))

                // Información del cliente
                Text(
                    text = "Información del Cliente:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nombre completo
                Text(
                    text = "👤 ${booking.getUserFullName()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tipo y número de documento
                if (!booking.userDocumentType.isNullOrBlank() && !booking.userDocumentNumber.isNullOrBlank()) {
                    Text(
                        text = "🆔 ${booking.userDocumentType}: ${booking.userDocumentNumber}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Teléfono
                if (!booking.userPhone.isNullOrBlank()) {
                    Text(
                        text = "📞 ${booking.userPhone}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Email
                if (!booking.userEmail.isNullOrBlank()) {
                    Text(
                        text = "✉️ ${booking.userEmail}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Dirección
                Text(
                    text = "📍 ${booking.address}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )

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

            booking.services.forEach { service ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${service.serviceName ?: "Servicio #${service.serviceId}"} x${service.quantity}",
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

            // Delivery
            val deliveryCost = booking.deliveryCost ?: 0.0
            if (deliveryCost > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delivery",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción - Solo mostrar si está pendiente
            if (booking.status.lowercase() == "pending") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onStatusChange(booking.id, "attended") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                    Button(
                        onClick = { onStatusChange(booking.id, "cancelled") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336)
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            } else {
                // Mostrar estado final si ya fue procesado
                when (booking.status.lowercase()) {
                    "attended" -> {
                        Text(
                            text = "✅ Reserva atendida",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    "cancelled" -> {
                        Text(
                            text = "❌ Reserva cancelada",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFF44336),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

