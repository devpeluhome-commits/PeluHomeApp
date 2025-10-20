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
    availableServices: List<com.peluhome.project.domain.model.Service> = emptyList(),
    onNavigateToRequests: () -> Unit = {},
    onCartUpdated: (Map<Int, Int>) -> Unit = {},
    onCartQuantityChange: ((Int, Int) -> Unit) -> Unit = {},
    onServicesListUpdated: (List<com.peluhome.project.domain.model.Service>) -> Unit = {},
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
    
    // Debug: Log del estado del error
    LaunchedEffect(servicesState.error) {
        println("DEBUG ServicesScreen: Estado del error cambió: ${servicesState.error}")
    }
    
    // Fallback: Verificar errores periódicamente
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000) // Verificar cada segundo
            if (servicesState.error != null && !showErrorDialog) {
                println("DEBUG ServicesScreen: Error detectado en fallback: ${servicesState.error}")
                showErrorDialog = true
                errorMessage = servicesState.error
            }
        }
    }
    
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
            println("DEBUG ServicesScreen: Error detectado: ${servicesState.error}")
            showErrorDialog = true
            errorMessage = servicesState.error
        }
    }
    
    // Escuchar cuando la reserva se crea exitosamente
    LaunchedEffect(servicesState.bookingCreated) {
        if (servicesState.bookingCreated != null) {
            showErrorDialog = true
            errorMessage = "¡Servicio solicitado exitosamente! Puedes ver tu reserva en 'Mis Solicitudes'."
            
            // Limpiar el estado de booking creado
            servicesViewModel.clearBookingCreated()
            
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
    
    // Cargar servicios desde API cuando se selecciona una categoría
    LaunchedEffect(selectedCategoryId) {
        selectedCategoryId?.let {
            servicesViewModel.loadServicesByCategory(it)
        }
    }
    
    // Notificar cambios en la lista de servicios al HomeScreen
    LaunchedEffect(servicesState.services) {
        onServicesListUpdated(servicesState.services)
    }
    
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
                            services = servicesState.services,
                            isLoading = servicesState.isLoadingServices,
                            selectedServices = selectedServices,
                            onServiceQuantityChange = handleQuantityChange
                        )
                    }
                
                    3 -> {
                        val serviceQuantityMap = selectedServices.mapNotNull { (serviceId, quantity) ->
                            // Buscar el servicio en la lista de servicios disponibles (de todas las categorías)
                            val service = availableServices.find { it.id == serviceId }
                            service?.let { 
                                serviceId to ServiceQuantity(
                                    Service(
                                        id = it.id,
                                        name = it.name,
                                        price = it.price,
                                        duration = "${it.durationMinutes} min"
                                    ),
                                    quantity
                                )
                            }
                        }.toMap()
                        
                        println("DEBUG Step3: Servicios seleccionados: ${selectedServices.size}")
                        println("DEBUG Step3: Servicios disponibles: ${availableServices.size}")
                        println("DEBUG Step3: ServiceQuantityMap: ${serviceQuantityMap.size}")

                        Step3AdditionalData(
                            date = date,
                            time = time,
                            address = address,
                            comments = comments,
                        selectedServices = serviceQuantityMap,
                            dateError = dateError,
                            timeError = timeError,
                            addressError = addressError,
                            isLoading = servicesState.isCreatingBooking,
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
                                    // Convertir fecha de DD/MM/YYYY a YYYY-MM-DD
                                    val dateParts = date.split("/")
                                    val formattedDate = "${dateParts[2]}-${dateParts[1]}-${dateParts[0]}"
                                    
                                    // Preparar lista de servicios (serviceId, quantity)
                                    val servicesList = selectedServices.map { (serviceId, quantity) ->
                                        serviceId to quantity
                                    }
                                    
                                    // Preparar mapa de precios
                                    val servicesPrices = selectedServices.mapNotNull { (serviceId, _) ->
                                        val service = availableServices.find { it.id == serviceId }
                                        service?.let { serviceId to it.price }
                                    }.toMap()
                                    
                                    // Llamar al API para crear la reserva
                                    servicesViewModel.createBooking(
                                        serviceDate = formattedDate,
                                        serviceTime = time,
                                        address = address,
                                        services = servicesList,
                                        servicesPrices = servicesPrices,
                                        notes = comments.ifBlank { null }
                                    )
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

