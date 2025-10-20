package com.peluhome.project.presentation.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorPrimary
import com.peluhome.project.utils.DeliveryPricing
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.background
import peluhome.composeapp.generated.resources.icon_business_center

// Data class para los servicios de un pedido
data class RequestService(
    val serviceName: String,
    val quantity: Int,
    val price: Double
)

// Data class para un pedido
data class ServiceRequest(
    val orderNumber: String,
    val date: String,
    val time: String,
    val address: String,
    val status: String,
    val services: List<RequestService>
)

@Composable
fun MyRequestsScreen() {
    // Datos ficticios de pedidos
    val requests = listOf(
        ServiceRequest(
            orderNumber = "PED-001",
            date = "2025-10-15",
            time = "14:00",
            address = "Av. Principal 123, Lima",
            status = "Atendido",
            services = listOf(
                RequestService("Corte de cabello", 1, 30.00),
                RequestService("Manicure", 1, 25.00)
            )
        ),
        ServiceRequest(
            orderNumber = "PED-002",
            date = "2025-10-18",
            time = "10:30",
            address = "Jr. Los Olivos 456, San Isidro",
            status = "Pendiente",
            services = listOf(
                RequestService("Maquillaje profesional", 1, 80.00),
                RequestService("Peinado", 1, 40.00)
            )
        ),
        ServiceRequest(
            orderNumber = "PED-003",
            date = "2025-10-20",
            time = "16:00",
            address = "Calle Las Flores 789, Miraflores",
            status = "Pendiente",
            services = listOf(
                RequestService("Tratamiento de keratina", 1, 150.00),
                RequestService("Corte de cabello", 1, 30.00),
                RequestService("Tinte", 1, 60.00)
            )
        )
    )
    
    var selectedRequest by remember { mutableStateOf<ServiceRequest?>(null) }
    var showCancelDialog by remember { mutableStateOf<ServiceRequest?>(null) }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Mis Solicitudes",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            
            if (requests.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes solicitudes registradas",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(requests) { request ->
                        RequestCard(
                            request = request,
                            onClick = { selectedRequest = request },
                            onCancel = { showCancelDialog = request }
                        )
                    }
                }
            }
        }
        
        // Diálogo de detalles del pedido
        selectedRequest?.let { request ->
            RequestDetailsDialog(
                request = request,
                onDismiss = { selectedRequest = null }
            )
        }
        
        // Diálogo de confirmación de cancelación
        showCancelDialog?.let { request ->
            AlertDialog(
                onDismissRequest = { showCancelDialog = null },
                title = {
                    Text(
                        text = "Cancelar Solicitud",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text("¿Estás seguro que deseas cancelar esta solicitud? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Aquí iría la lógica para cancelar la solicitud
                            println("DEBUG: Cancelando solicitud ${request.orderNumber}")
                            showCancelDialog = null
                            // TODO: Actualizar el estado del request a "Cancelado"
                        }
                    ) {
                        Text(
                            text = "Sí, cancelar",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showCancelDialog = null }
                    ) {
                        Text(
                            text = "No",
                            color = ColorPrimary
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun RequestCard(
    request: ServiceRequest,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido: ${request.orderNumber}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                StatusBadge(status = request.status)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "calendar",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${request.date} - ${request.time}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "location",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF666666)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = request.address,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    maxLines = 1
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (request.status == "Pendiente") {
                    Text(
                        text = "Cancelar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        modifier = Modifier
                            .clickable { onCancel() }
                            .padding(end = 16.dp)
                    )
                }
                
                Text(
                    text = "Ver detalles →",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorPrimary,
                    modifier = Modifier.clickable { onClick() }
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Atendido" -> Color(0xFF4CAF50) to Color.White // Verde para completado
        "Pendiente" -> Color(0xFFFFA726) to Color.White // Naranja para pendiente
        else -> Color.Gray to Color.White // Gris por defecto
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = status,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
private fun RequestDetailsDialog(
    request: ServiceRequest,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detalles del Pedido",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorPrimary
                )
                Text(
                    text = request.orderNumber,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Información del servicio
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        InfoRow(label = "Fecha:", value = request.date)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(label = "Hora:", value = request.time)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(label = "Dirección:", value = request.address)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estado:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF666666)
                            )
                            StatusBadge(status = request.status)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Resumen de servicios
                Text(
                    text = "Resumen de Servicios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                request.services.forEach { service ->
                    ServiceSummaryItem(
                        serviceName = service.serviceName,
                        quantity = service.quantity,
                        price = service.price
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Agregar costo de delivery
                val deliveryCost = DeliveryPricing.calculateDeliveryCost(request.date, request.time)
                val deliveryDescription = DeliveryPricing.getDeliveryDescription(request.date, request.time)
                
                ServiceSummaryItem(
                    serviceName = deliveryDescription,
                    quantity = 1,
                    price = deliveryCost
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                
                // Total
                val servicesTotal = request.services.sumOf { it.price * it.quantity }
                val totalWithDelivery = servicesTotal + deliveryCost
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "S/ $totalWithDelivery",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cerrar",
                    color = ColorPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun ServiceSummaryItem(
    serviceName: String,
    quantity: Int,
    price: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = serviceName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "Cantidad: $quantity",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Text(
            //text = "S/ ${"%.2f".format(price * quantity)}",
            text = "S/ ${price * quantity}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorPrimary
        )
    }
}

