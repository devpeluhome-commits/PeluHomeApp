package com.peluhome.project.presentation.home.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_minus
import peluhome.composeapp.generated.resources.icon_plus

data class Service(
    val id: Int,
    val name: String,
    val price: Double,
    val duration: String
)

data class ServiceQuantity(
    val service: Service,
    val quantity: Int
)

@Composable
fun Step2ServiceSelection(
    categoryId: Int,
    selectedServices: Map<Int, Int>,
    onServiceQuantityChange: (Int, Int) -> Unit
) {
    println("DEBUG Step2: CategoryId recibido = $categoryId")
    val services = getServicesByCategory(categoryId)
    println("DEBUG Step2: Servicios encontrados = ${services.size}")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(services) { service ->
                ServiceCard(
                    service = service,
                    quantity = selectedServices[service.id] ?: 0,
                    onQuantityChange = { newQuantity ->
                        onServiceQuantityChange(service.id, newQuantity)
                    }
                )
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: Service,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (quantity > 0) 2.dp else 1.dp,
                color = if (quantity > 0) ColorPrimary else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.name,
                    fontSize = 16.sp,
                    fontWeight = if (quantity > 0) FontWeight.Bold else FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "S/ ${service.price}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (quantity > 0) ColorPrimary else Color(0xFF666666)
                )
                Text(
                    text = service.duration,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (quantity > 0) ColorPrimary else Color.LightGray)
                        .clickable {
                            if (quantity > 0) {
                                onQuantityChange(quantity - 1)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_minus),
                        contentDescription = "Disminuir",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Text(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (quantity > 0) Color.Black else Color.Gray,
                    modifier = Modifier.width(30.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ColorPrimary)
                        .clickable { onQuantityChange(quantity + 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_plus),
                        contentDescription = "Aumentar",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

private fun getServicesByCategory(categoryId: Int): List<Service> {
    println("DEBUG getServicesByCategory: Buscando servicios para categoryId=$categoryId")
    val result = when (categoryId) {
        1 -> listOf( // MAQUILLAJE (4 servicios)
            Service(1, "Maquillaje de día", 45.0, "1 hora"),
            Service(2, "Maquillaje de noche", 65.0, "1.5 horas"),
            Service(3, "Maquillaje de novia", 120.0, "2 horas"),
            Service(4, "Maquillaje para eventos", 80.0, "1.5 horas")
        )

        2 -> listOf( // MANICURE Y PEDICURE (3 servicios)
            Service(5, "Manicure básica", 25.0, "45 min"),
            Service(6, "Pedicure completa", 35.0, "1 hora"),
            Service(7, "Manicure + Pedicure", 50.0, "1.5 horas")
        )

        3 -> listOf( // TRATAMIENTO DE CABELLO (4 servicios)
            Service(8, "Corte de cabello", 30.0, "45 min"),
            Service(9, "Tinte completo", 80.0, "2 horas"),
            Service(10, "Tratamiento de keratina", 150.0, "3 horas"),
            Service(11, "Peinado para eventos", 55.0, "1 hora")
        )

        4 -> listOf( // DEPILACIÓN (3 servicios)
            Service(12, "Depilación de piernas", 35.0, "1 hora"),
            Service(13, "Depilación de axilas", 15.0, "20 min"),
            Service(14, "Depilación de bikini", 25.0, "45 min")
        )

        else -> {
            println("DEBUG getServicesByCategory: categoryId=$categoryId NO COINCIDE con ningún caso")
            emptyList()
        }
    }
    println("DEBUG getServicesByCategory: Retornando ${result.size} servicios")
    return result
}

