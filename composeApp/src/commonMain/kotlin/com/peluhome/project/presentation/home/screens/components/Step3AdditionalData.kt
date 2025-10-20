package com.peluhome.project.presentation.home.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.components.CustomButton
import com.peluhome.project.presentation.components.MessageValidationComponent
import com.peluhome.project.presentation.components.OutlinedTextFieldComponentBasic
import com.peluhome.project.ui.ColorPrimary
import com.peluhome.project.utils.DeliveryPricing
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_business_center

@Composable
fun Step3AdditionalData(
    date: String,
    time: String,
    address: String,
    comments: String = "",
    selectedServices: Map<Int, ServiceQuantity>,
    dateError: String? = null,
    timeError: String? = null,
    addressError: String? = null,
    isLoading: Boolean = false,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onCommentsChange: (String) -> Unit = {},
    onDatePickerClick: () -> Unit,
    onTimePickerClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    
    // Función para calcular el total con delivery
    val calculateTotalWithDelivery = {
        val servicesTotal = selectedServices.values.sumOf { serviceQuantity ->
            serviceQuantity.service.price * serviceQuantity.quantity
        }
        
        val deliveryCost = if (date.isNotEmpty() && time.isNotEmpty()) {
            DeliveryPricing.calculateDeliveryCost(date, time)
        } else {
            0.0
        }
        
        println("DEBUG Step3: Servicios seleccionados: ${selectedServices.size}")
        println("DEBUG Step3: Total servicios: $servicesTotal")
        println("DEBUG Step3: Fecha: $date, Hora: $time")
        println("DEBUG Step3: Costo delivery: $deliveryCost")
        println("DEBUG Step3: Total final: ${servicesTotal + deliveryCost}")
        
        servicesTotal + deliveryCost
    }
    
    val total = calculateTotalWithDelivery()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo de fecha con picker
        Column {
            OutlinedTextFieldComponentBasic(
                text = date,
                onValueChange = { }, // No permite edición manual
                textLabel = "Fecha (DD/MM/YYYY)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = dateError != null,
                enabled = false, // Deshabilita la edición manual
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_business_center),
                        contentDescription = "date icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    Text(
                        text = "📅",
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.clickable { onDatePickerClick() }
            )
            if (dateError != null) {
                MessageValidationComponent(field = dateError)
            }
            // Nota informativa
            Text(
                text = "* Recuerde que la fecha seleccionada debe ser igual o posterior al día de hoy.",
                fontSize = 10.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(start = 4.dp, top = 2.dp),
                lineHeight = 12.sp
            )
        }

        // Campo de hora con picker
        Column {
            OutlinedTextFieldComponentBasic(
                text = time,
                onValueChange = { }, // No permite edición manual
                textLabel = "Hora (HH:MM)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                isError = timeError != null,
                enabled = false, // Deshabilita la edición manual
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_business_center),
                        contentDescription = "time icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    Text(
                        text = "🕐",
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.clickable { onTimePickerClick() }
            )
            if (timeError != null) {
                MessageValidationComponent(field = timeError)
            }
        }

        // Campo de dirección
        Column {
            OutlinedTextFieldComponentBasic(
                text = address,
                onValueChange = onAddressChange,
                textLabel = "Dirección",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                isError = addressError != null,
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_business_center),
                        contentDescription = "address icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
            )
            if (addressError != null) {
                MessageValidationComponent(field = addressError)
            }
        }

        // Campo de comentarios (opcional)
        Column {
            OutlinedTextFieldComponentBasic(
                text = comments,
                onValueChange = onCommentsChange,
                textLabel = "Comentarios (opcional)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_business_center),
                        contentDescription = "comments icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                },
                isError = false
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Resumen
        Text(
            text = "Resumen de servicios",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
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
                selectedServices.values.forEach { serviceQuantity ->
                    ServiceSummaryItem(
                        serviceName = serviceQuantity.service.name,
                        quantity = serviceQuantity.quantity,
                        price = serviceQuantity.service.price
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Mostrar costo de delivery si hay fecha y hora seleccionada
                if (date.isNotEmpty() && time.isNotEmpty()) {
                    val deliveryCost = DeliveryPricing.calculateDeliveryCost(date, time)
                    val deliveryDescription = DeliveryPricing.getDeliveryDescription(date, time)
                    
                    ServiceSummaryItem(
                        serviceName = deliveryDescription,
                        quantity = 1,
                        price = deliveryCost
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

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
                        text = "S/$total",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CustomButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Solicitar Servicio",
            onClick = onSubmit,
            isLoading = isLoading,
            color = ColorPrimary
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
            text = "S/ ${price * quantity}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorPrimary
        )
    }
}