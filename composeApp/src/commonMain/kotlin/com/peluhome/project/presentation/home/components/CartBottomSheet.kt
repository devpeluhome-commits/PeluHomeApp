package com.peluhome.project.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_minus
import peluhome.composeapp.generated.resources.icon_plus

/**
 * Función auxiliar para convertir un servicio del dominio al modelo local de UI
 */
private fun convertToUIService(domainService: com.peluhome.project.domain.model.Service): com.peluhome.project.presentation.home.screens.components.Service {
    return com.peluhome.project.presentation.home.screens.components.Service(
        id = domainService.id,
        name = domainService.name,
        price = domainService.price,
        duration = "${domainService.durationMinutes} min"
    )
}

@Composable
fun CartBottomSheet(
    selectedServicesMap: Map<Int, Int>,
    availableServices: List<com.peluhome.project.domain.model.Service>,
    onDismiss: () -> Unit,
    onQuantityChange: (serviceId: Int, quantity: Int) -> Unit = { _, _ -> }
) {
    println("DEBUG CartBottomSheet: Mostrando carrito con ${selectedServicesMap.size} servicios: $selectedServicesMap")
    println("DEBUG CartBottomSheet: Servicios disponibles: ${availableServices.size}")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Servicios Seleccionados",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (selectedServicesMap.isEmpty()) {
                    Text(
                        text = "No hay servicios seleccionados",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    // Agrupar servicios por categoría
                    val servicesByCategory = selectedServicesMap.mapNotNull { (serviceId, quantity) ->
                        val domainService = availableServices.find { it.id == serviceId }
                        domainService?.let { 
                            val uiService = convertToUIService(it)
                            serviceId to CartServiceItem(uiService, quantity)
                        }
                    }.groupBy { (_, serviceItem) ->
                        // Buscar la categoría del servicio
                        val domainService = availableServices.find { it.id == serviceItem.service.id }
                        domainService?.categoryId?.let { categoryId ->
                            // Aquí podrías tener un mapa de IDs de categoría a nombres
                            // Por ahora usamos el categoryId directamente
                            when (categoryId) {
                                1 -> "Maquillaje"
                                2 -> "Manicure y Pedicure"
                                3 -> "Tratamiento de Cabello"
                                4 -> "Depilación"
                                else -> "Otros"
                            }
                        } ?: "Otros"
                    }
                    
                    var totalAmount = 0.0
                    
                    servicesByCategory.forEach { (categoryName, services) ->
                        // Título de la categoría
                        Text(
                            text = categoryName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        // Servicios de la categoría
                        services.forEach { (_, serviceItem) ->
                            val serviceTotal = serviceItem.service.price * serviceItem.quantity
                            totalAmount += serviceTotal
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Nombre del servicio
                                Text(
                                    text = serviceItem.service.name,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    modifier = Modifier.weight(1f)
                                )
                                
                                // Controles de cantidad
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Botón menos
                                    IconButton(
                                        onClick = {
                                            val newQuantity = serviceItem.quantity - 1
                                            if (newQuantity <= 0) {
                                                onQuantityChange(serviceItem.service.id, 0)
                                            } else {
                                                onQuantityChange(serviceItem.service.id, newQuantity)
                                            }
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.icon_minus),
                                            contentDescription = "Menos",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.Black
                                        )
                                    }
                                    
                                    // Cantidad
                                    Text(
                                        text = serviceItem.quantity.toString(),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.width(20.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    // Botón más
                                    IconButton(
                                        onClick = {
                                            onQuantityChange(serviceItem.service.id, serviceItem.quantity + 1)
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.icon_plus),
                                            contentDescription = "Más",
                                            modifier = Modifier.size(12.dp),
                                            tint = Color.Black
                                        )
                                    }
                                    
                                    // Precio total del servicio
                                    Text(
                                        text = "S/ $serviceTotal",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorPrimary,
                                        modifier = Modifier.width(60.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )
                    
                    // Total
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
                            text = "S/ $totalAmount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
            ) {
                Text(
                    text = "Cerrar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

data class CartServiceItem(
    val service: com.peluhome.project.presentation.home.screens.components.Service,
    val quantity: Int
)
