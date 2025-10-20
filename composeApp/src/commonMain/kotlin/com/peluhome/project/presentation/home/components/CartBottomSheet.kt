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
 * Función auxiliar para obtener servicios por categoría
 * (Duplicada de ServicesScreen para evitar dependencias circulares)
 */
private fun getServiceById(serviceId: Int): com.peluhome.project.presentation.home.screens.components.Service? {
    val allServices = listOf(
        // MAQUILLAJE (4 servicios)
        com.peluhome.project.presentation.home.screens.components.Service(1, "Maquillaje de día", 45.0, "1 hora"),
        com.peluhome.project.presentation.home.screens.components.Service(2, "Maquillaje de noche", 65.0, "1.5 horas"),
        com.peluhome.project.presentation.home.screens.components.Service(3, "Maquillaje de novia", 120.0, "2 horas"),
        com.peluhome.project.presentation.home.screens.components.Service(4, "Maquillaje para eventos", 80.0, "1.5 horas"),

        // MANICURE Y PEDICURE (3 servicios)
        com.peluhome.project.presentation.home.screens.components.Service(5, "Manicure básica", 25.0, "45 min"),
        com.peluhome.project.presentation.home.screens.components.Service(6, "Pedicure completa", 35.0, "1 hora"),
        com.peluhome.project.presentation.home.screens.components.Service(7, "Manicure + Pedicure", 50.0, "1.5 horas"),

        // TRATAMIENTO DE CABELLO (4 servicios)
        com.peluhome.project.presentation.home.screens.components.Service(8, "Corte de cabello", 30.0, "45 min"),
        com.peluhome.project.presentation.home.screens.components.Service(9, "Tinte completo", 80.0, "2 horas"),
        com.peluhome.project.presentation.home.screens.components.Service(10, "Tratamiento de keratina", 150.0, "3 horas"),
        com.peluhome.project.presentation.home.screens.components.Service(11, "Peinado para eventos", 55.0, "1 hora"),

        // DEPILACIÓN (3 servicios)
        com.peluhome.project.presentation.home.screens.components.Service(12, "Depilación de piernas", 35.0, "1 hora"),
        com.peluhome.project.presentation.home.screens.components.Service(13, "Depilación de axilas", 15.0, "20 min"),
        com.peluhome.project.presentation.home.screens.components.Service(14, "Depilación de bikini", 25.0, "45 min")
    )
    return allServices.find { it.id == serviceId }
}

/**
 * Función auxiliar para obtener el nombre de la categoría por ID de servicio
 */
private fun getCategoryNameByServiceId(serviceId: Int): String {
    return when (serviceId) {
        in 1..4 -> "Maquillaje"
        in 5..7 -> "Manicure y Pedicure"
        in 8..11 -> "Tratamiento de Cabello"
        in 12..14 -> "Depilación"
        else -> "Otros"
    }
}

@Composable
fun CartBottomSheet(
    selectedServicesMap: Map<Int, Int>,
    onDismiss: () -> Unit,
    onQuantityChange: (serviceId: Int, quantity: Int) -> Unit = { _, _ -> }
) {
    println("DEBUG CartBottomSheet: Mostrando carrito con ${selectedServicesMap.size} servicios: $selectedServicesMap")
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
                        val service = getServiceById(serviceId)
                        service?.let { serviceId to CartServiceItem(it, quantity) }
                    }.groupBy { (_, serviceItem) ->
                        getCategoryNameByServiceId(serviceItem.service.id)
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
