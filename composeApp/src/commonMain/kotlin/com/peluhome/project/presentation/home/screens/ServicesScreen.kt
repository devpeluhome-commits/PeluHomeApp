package com.peluhome.project.presentation.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.presentation.components.DatePickerDialog
import com.peluhome.project.presentation.components.TimePickerDialog
import com.peluhome.project.presentation.home.ServicesViewModel
import com.peluhome.project.presentation.home.screens.components.*
import com.peluhome.project.ui.ColorPrimary
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.background
import peluhome.composeapp.generated.resources.icon_back
import peluhome.composeapp.generated.resources.icon_next

@Composable
fun ServicesScreen(
    servicesViewModel: ServicesViewModel = koinViewModel(),
    onNavigateToRequests: () -> Unit = {},
    onCartUpdated: (Map<Int, Int>) -> Unit = {},
    onCartQuantityChange: ((Int, Int) -> Unit) -> Unit = {},
    onServiceCompleted: () -> Unit = {}
) {
    val servicesState = servicesViewModel.state
    
    var currentStep by remember { mutableStateOf(1) }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var selectedServices by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    
    var dateError by remember { mutableStateOf<String?>(null) }
    var timeError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }
    
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Validación de fecha y hora
    fun validateStep3(): Boolean {
        var isValid = true
        
        if (date.isEmpty()) {
            dateError = "Seleccione una fecha"
            isValid = false
        } else {
            // Validar que la fecha no sea anterior a hoy
            try {
                val dateParts = date.split("/")
                if (dateParts.size == 3) {
                    val day = dateParts[0].toInt()
                    val month = dateParts[1].toInt()
                    val year = dateParts[2].toInt()
                    
                    val selectedDate = kotlinx.datetime.LocalDate(year, month, day)
                    val today = kotlinx.datetime.Clock.System.now()
                        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                        .date
                    
                    if (selectedDate < today) {
                        dateError = "La fecha no puede ser anterior a hoy"
                        isValid = false
                    }
                }
            } catch (e: Exception) {
                dateError = "Formato de fecha inválido"
                isValid = false
            }
        }
        
        if (time.isEmpty()) {
            timeError = "Seleccione una hora"
            isValid = false
        }
        
        if (address.isEmpty()) {
            addressError = "Ingrese una dirección"
            isValid = false
        }
        
        return isValid
    }
    
    // Función para cambiar cantidad de servicios
    val handleQuantityChange: (Int, Int) -> Unit = { serviceId, quantity ->
        selectedServices = if (quantity > 0) {
            selectedServices + (serviceId to quantity)
        } else {
            selectedServices - serviceId
        }
    }
    
    // Notificar cambios en el carrito al HomeScreen
    LaunchedEffect(selectedServices) {
        println("DEBUG ServicesScreen: Notificando carrito actualizado: $selectedServices")
        onCartUpdated(selectedServices)
    }
    
    // Pasar la función de cambio de cantidad al HomeScreen
    LaunchedEffect(Unit) {
        onCartQuantityChange(handleQuantityChange)
    }
    
    LaunchedEffect(servicesState.error) {
        if (servicesState.error != null) {
            showErrorDialog = true
            errorMessage = servicesState.error
        }
    }
    
    // TODO: Cargar servicios desde API cuando se selecciona una categoría
    // Por ahora usamos datos locales
    // LaunchedEffect(selectedCategoryId) {
    //     selectedCategoryId?.let {
    //         servicesViewModel.loadServicesByCategory(it)
    //     }
    // }
    
    if (showErrorDialog) {
        // Determinar si es un error real o información
        val isRealError = servicesState.error != null
        
        AlertComponent(
            title = if (isRealError) "Error" else "Información",
            message = errorMessage,
            dismiss = {
                showErrorDialog = false
                if (isRealError) servicesViewModel.clearError()
            },
            action = {
                showErrorDialog = false
                if (isRealError) servicesViewModel.clearError()
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Indicador de pasos
            StepIndicator(
                currentStep = currentStep,
                totalSteps = 3,
                stepTitles = listOf("Tipo", "Servicios", "Adicionales")
            )

            // Contenido según el paso actual
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (currentStep) {
                            1 -> Step1CategorySelection(
                    categories = servicesState.categories,
                                selectedCategoryId = selectedCategoryId,
                    isLoading = servicesState.isLoadingCategories,
                                onCategorySelected = { categoryId ->
                                    selectedCategoryId = categoryId
                                }
                            )
                
                    2 -> selectedCategoryId?.let { categoryId ->
                        Step2ServiceSelection(
                            categoryId = categoryId,
                        selectedServices = selectedServices,
                        onServiceQuantityChange = handleQuantityChange
                    )
                }
                
                    3 -> {
                    val serviceQuantityMap = selectedServices.mapNotNull { (serviceId, quantity) ->
                        // Buscar el servicio en los datos locales
                            val service = getServiceById(serviceId)
                            service?.let { serviceId to ServiceQuantity(it, quantity) }
                        }.toMap()

                        Step3AdditionalData(
                            date = date,
                            time = time,
                            address = address,
                            comments = comments,
                        selectedServices = serviceQuantityMap,
                            dateError = dateError,
                            timeError = timeError,
                            addressError = addressError,
                        onDateChange = { 
                            date = it
                            dateError = null
                        },
                        onTimeChange = { 
                            time = it
                            timeError = null
                        },
                        onAddressChange = { 
                            address = it
                            addressError = null
                        },
                            onCommentsChange = { comments = it },
                        onDatePickerClick = { 
                            showDatePicker = true 
                            println("DEBUG: Abriendo Date Picker")
                        },
                        onTimePickerClick = { 
                            showTimePicker = true 
                            println("DEBUG: Abriendo Time Picker")
                        },
                            onSubmit = {
                                if (validateStep3()) {
                                // TODO: Llamar al API para crear la reserva
                                showErrorDialog = true
                                errorMessage = "¡Servicio solicitado exitosamente!"
                                
                                // Resetear el formulario
                                currentStep = 1
                                selectedCategoryId = null
                                selectedServices = emptyMap()
                                date = ""
                                time = ""
                                address = ""
                                comments = ""
                                
                                onServiceCompleted()
                                }
                            }
                        )
                    }
                }
            }

        // Botones de navegación posicionados en las esquinas
        Box(
                modifier = Modifier
                    .fillMaxWidth()
                .padding(16.dp)
            ) {
            // Botón Atrás (esquina inferior izquierda)
                if (currentStep > 1) {
                    Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    FloatingActionButton(
                        onClick = { currentStep-- },
                        containerColor = ColorPrimary,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(peluhome.composeapp.generated.resources.Res.drawable.icon_back),
                            contentDescription = "Atrás",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Atrás",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
            
            // Botón Siguiente (esquina inferior derecha)
            if (currentStep < 3) {
                        Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    FloatingActionButton(
                        onClick = {
                            when (currentStep) {
                                1 -> {
                                    if (selectedCategoryId != null) {
                                        currentStep++
                                    } else {
                                        showErrorDialog = true
                                        errorMessage = "Seleccione una categoría"
                                    }
                                }
                                2 -> {
                                    if (selectedServices.isNotEmpty()) {
                                        currentStep++
                    } else {
                                        showErrorDialog = true
                                        errorMessage = "Seleccione al menos un servicio"
                                    }
                                }
                            }
                        },
                        containerColor = ColorPrimary,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(peluhome.composeapp.generated.resources.Res.drawable.icon_next),
                            contentDescription = "Siguiente",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Siguiente",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }
        }
        }
    }
    
    // DatePicker nativo (Android/iOS)
    DatePickerDialog(
        show = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { selectedDate ->
            date = selectedDate
            dateError = null
            println("DEBUG: Fecha seleccionada: $selectedDate")
        }
    )
    
    // TimePicker nativo (Android/iOS)
    TimePickerDialog(
        show = showTimePicker,
        onDismiss = { showTimePicker = false },
        onTimeSelected = { selectedTime ->
            time = selectedTime
            timeError = null
            println("DEBUG: Hora seleccionada: $selectedTime")
        }
    )
}

// Función auxiliar para obtener un servicio por ID
private fun getServiceById(serviceId: Int): Service? {
    // Lista completa de servicios (deberías tener esto en un lugar centralizado)
    val allServices = listOf(
        // CORTE DE CABELLO
        Service(1, "Puntas", 35.0, "30 min"),
        Service(2, "Puntas regular", 50.0, "45 min"),
        Service(3, "Cambio de look", 80.0, "1 hora"),
        Service(4, "Hombres/niños", 35.0, "30 min"),
        Service(5, "Corte puntas + Cepillado", 65.0, "45 min"),
        // PEINADOS
        Service(6, "Peinado de mujer", 50.0, "1 hora"),
        Service(7, "Moño de mujer", 80.0, "1.5 horas"),
        Service(8, "Peinado de niña", 45.0, "45 min"),
        Service(9, "Cepillado", 40.0, "30 min"),
        Service(10, "Planchado", 40.0, "45 min"),
        Service(11, "Ondas/Bucles", 45.0, "1 hora"),
        // TRATAMIENTOS DE CABELLO
        Service(12, "Aplicación de tinte", 35.0, "1 hora"),
        Service(13, "Tinte (raíces)", 95.0, "1.5 horas"),
        Service(14, "Tinte (completo, sin decoloración)", 115.0, "2 horas"),
        Service(15, "Tinte (full colores, mechas)", 60.0, "2.5 horas"),
        Service(16, "Reacondicionamiento", 120.0, "1.5 horas"),
        Service(17, "Botox cabello corto", 130.0, "2 horas"),
        Service(18, "Botox cabello mediano", 150.0, "2.5 horas"),
        Service(19, "Botox cabello largo", 185.0, "3 horas"),
        Service(20, "Alisado con Keratina", 245.0, "4 horas"),
        Service(21, "Iluminación", 155.0, "2 horas"),
        Service(22, "Baño de color", 80.0, "1.5 horas"),
        // CLÁSICAS
        Service(23, "Manicure", 35.0, "45 min"),
        Service(24, "Manicure gel frío", 45.0, "1 hora"),
        Service(25, "Manicure gel lámpara", 50.0, "1 hora"),
        Service(26, "Manicure Rubber", 55.0, "1.25 horas"),
        Service(27, "Manicure Rubber gel color", 75.0, "1.5 horas"),
        Service(28, "Pedicure", 45.0, "1 hora"),
        Service(29, "Pedicure gel frío", 55.0, "1.25 horas"),
        Service(30, "Pedicure gel lámpara", 60.0, "1.25 horas"),
        // ACRÍLICAS
        Service(31, "Uñas acrílicas con gel", 130.0, "2 horas"),
        Service(32, "Gel de reconstrucción", 120.0, "1.5 horas"),
        Service(33, "Mantenimiento acrílicas", 110.0, "1.5 horas"),
        // EXTRAS
        Service(34, "Retiro acrílico", 30.0, "45 min"),
        Service(35, "Uña acrílica adicional", 20.0, "15 min"),
        Service(36, "Diseño adicional (02 uñas)", 10.0, "20 min"),
        Service(37, "Retiro de gel", 20.0, "30 min"),
        Service(38, "Pintado de uñas", 15.0, "30 min"),
        // DEPILACIÓN
        Service(39, "Cejas", 20.0, "15 min"),
        Service(40, "Bozo", 20.0, "10 min"),
        Service(41, "Rostro completo", 80.0, "45 min"),
        Service(42, "Axilas", 35.0, "20 min"),
        Service(43, "Media pierna", 35.0, "30 min"),
        Service(44, "Pierna completa", 70.0, "1 hora"),
        Service(45, "Brasilera", 70.0, "45 min"),
        Service(46, "Bikini (línea)", 55.0, "30 min"),
        Service(47, "Con hilo por zona", 20.0, "15 min"),
        // MASAJES
        Service(48, "Relajantes", 110.0, "1 hora"),
        Service(49, "Descontracturantes", 110.0, "1 hora"),
        Service(50, "Reductores", 120.0, "1.25 horas"),
        // PAQUETE VERANO
        Service(51, "10 masajes reductores", 850.0, "12.5 horas")
    )
    
    return allServices.find { it.id == serviceId }
}
