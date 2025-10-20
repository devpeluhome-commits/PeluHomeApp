package com.peluhome.project.presentation.register_user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.presentation.register_user.components.RegisterHeader
import com.peluhome.project.presentation.register_user.components.RegisterForm
import com.peluhome.project.presentation.register_user.components.RegisterFooter
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.background
import peluhome.composeapp.generated.resources.icon_back

@Composable
fun RegisterUserScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterUserViewModel = koinViewModel(),
    onNavigationBack: () -> Unit,
    onNavigationHome: () -> Unit
) {
    val state = viewModel.state
    // Estados para los campos del formulario
    var names by remember { mutableStateOf("") }
    var paternalSurname by remember { mutableStateOf("") }
    var maternalSurname by remember { mutableStateOf("") }
    var documentType by remember { mutableStateOf("") }
    var documentNumber by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(true) }
    var repeatPasswordVisible by remember { mutableStateOf(true) }

    // Estado para el diálogo de confirmación
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Estado para errores de la API
    var showDialogError by remember { mutableStateOf(false) }
    var messageError by remember { mutableStateOf("") }
    
    // Efecto para manejar la respuesta del servidor
    LaunchedEffect(key1 = state.success, key2 = state.error) {
        if (state.success != null) {
            viewModel.clear()
            showSuccessDialog = true
        }
        if (state.error != null) {
            showDialogError = true
            messageError = state.error
        }
    }
    
    // Diálogo de error
    if (showDialogError) {
        AlertComponent(
            title = "ERROR",
            dismiss = {
                viewModel.clear()
                showDialogError = false
            },
            action = {
                viewModel.clear()
                showDialogError = false
            },
            message = messageError,
            textAlign = TextAlign.Center,
            paddingValues = PaddingValues(16.dp)
        )
    }

    // Estados para errores de validación
    var namesError by remember { mutableStateOf<String?>(null) }
    var paternalSurnameError by remember { mutableStateOf<String?>(null) }
    var maternalSurnameError by remember { mutableStateOf<String?>(null) }
    var documentTypeError by remember { mutableStateOf<String?>(null) }
    var documentNumberError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var repeatPasswordError by remember { mutableStateOf<String?>(null) }

    // Función para limpiar errores cuando el usuario empieza a escribir
    fun clearError(field: String) {
        when (field) {
            "names" -> namesError = null
            "paternalSurname" -> paternalSurnameError = null
            "maternalSurname" -> maternalSurnameError = null
            "documentType" -> documentTypeError = null
            "documentNumber" -> documentNumberError = null
            "phoneNumber" -> phoneNumberError = null
            "email" -> emailError = null
            "password" -> passwordError = null
            "repeatPassword" -> repeatPasswordError = null
        }
    }
    
    // Función para manejar el cambio de tipo de documento
    fun onDocumentTypeChange(newType: String) {
        documentType = newType
        // Limpiar el número de documento cuando cambie el tipo
        documentNumber = ""
        documentNumberError = null
    }
    
    // Función para validar y limitar la entrada del número de documento
    fun onDocumentNumberChange(newValue: String) {
        val maxLength = when (documentType) {
            "DNI" -> 8
            "RUC" -> 11
            "Carnet de Extranjería" -> 12
            else -> 20 // Por defecto
        }
        
        // Limitar la longitud
        if (newValue.length <= maxLength) {
            // Validar que solo contenga caracteres permitidos
            val isValidChar = when (documentType) {
                "DNI", "RUC" -> newValue.all { it.isDigit() }
                "Carnet de Extranjería" -> newValue.all { it.isLetterOrDigit() }
                else -> true
            }
            
            if (isValidChar) {
                documentNumber = newValue
                documentNumberError = null
            }
        }
    }

    // Función para validar todos los campos
    fun validateForm(): Boolean {
        var isValid = true

        // Limpiar errores previos
        namesError = null
        paternalSurnameError = null
        maternalSurnameError = null
        documentTypeError = null
        documentNumberError = null
        phoneNumberError = null
        emailError = null
        passwordError = null
        repeatPasswordError = null

        // Validar campos obligatorios
        if (names.isEmpty()) {
            namesError = "Debe ingresar sus nombres"
            isValid = false
        }

        if (paternalSurname.isEmpty()) {
            paternalSurnameError = "Debe ingresar su apellido paterno"
            isValid = false
        }

        if (maternalSurname.isEmpty()) {
            maternalSurnameError = "Debe ingresar su apellido materno"
            isValid = false
        }

        if (documentType.isEmpty()) {
            documentTypeError = "Debe seleccionar el tipo de documento"
            isValid = false
        }

        if (documentNumber.isEmpty()) {
            documentNumberError = "Debe ingresar su número de documento"
            isValid = false
        } else {
            // Validar número de documento según tipo
            when (documentType) {
                "DNI" -> {
                    if (documentNumber.length != 8 || !documentNumber.all { it.isDigit() }) {
                        documentNumberError = "El DNI debe tener 8 dígitos numéricos"
                        isValid = false
                    }
                }
                "RUC" -> {
                    if (documentNumber.length != 11 || !documentNumber.all { it.isDigit() }) {
                        documentNumberError = "El RUC debe tener 11 dígitos numéricos"
                        isValid = false
                    }
                }
                "Carnet de Extranjería" -> {
                    if (documentNumber.length != 12 || !documentNumber.all { it.isLetterOrDigit() }) {
                        documentNumberError = "El Carnet de Extranjería debe tener 12 caracteres alfanuméricos"
                        isValid = false
                    }
                }
            }
        }

        if (phoneNumber.isEmpty()) {
            phoneNumberError = "Debe ingresar su número de celular"
            isValid = false
        } else {
            // Validar número de celular (debe empezar en 9 y tener 9 dígitos)
            if (!phoneNumber.startsWith("9") || phoneNumber.length != 9 || !phoneNumber.all { it.isDigit() }) {
                phoneNumberError = "El número de celular debe empezar en 9 y tener 9 dígitos"
                isValid = false
            }
        }

        if (email.isEmpty()) {
            emailError = "Debe ingresar su correo electrónico"
            isValid = false
        } else {
            // Validar formato de email básico
            if (!email.contains("@") || !email.contains(".")) {
                emailError = "Debe ingresar un correo electrónico válido"
                isValid = false
            }
        }

        if (password.isEmpty()) {
            passwordError = "Debe ingresar una contraseña"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        }

        if (repeatPassword.isEmpty()) {
            repeatPasswordError = "Debe repetir la contraseña"
            isValid = false
        } else if (repeatPassword != password) {
            repeatPasswordError = "Las contraseñas no coinciden"
            isValid = false
        }

        return isValid
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
        ) { innerPadding ->
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
                        .padding(innerPadding)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Botón de retroceder
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_back),
                            contentDescription = "Retroceder",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable{
                                    onNavigationBack()
                                },
                            tint = Color.Unspecified
                        )
                    }

                    RegisterHeader()

                    //Spacer(modifier = Modifier.height(0.2.dp))

                    RegisterForm(
                        names = names,
                        paternalSurname = paternalSurname,
                        maternalSurname = maternalSurname,
                        documentType = documentType,
                        documentNumber = documentNumber,
                        phoneNumber = phoneNumber,
                        email = email,
                        password = password,
                        repeatPassword = repeatPassword,
                        passwordVisible = passwordVisible,
                        repeatPasswordVisible = repeatPasswordVisible,
                        namesError = namesError,
                        paternalSurnameError = paternalSurnameError,
                        maternalSurnameError = maternalSurnameError,
                        documentTypeError = documentTypeError,
                        documentNumberError = documentNumberError,
                        phoneNumberError = phoneNumberError,
                        emailError = emailError,
                        passwordError = passwordError,
                        repeatPasswordError = repeatPasswordError,
                        onNamesChange = { 
                            names = it
                            clearError("names")
                        },
                        onPaternalSurnameChange = { 
                            paternalSurname = it
                            clearError("paternalSurname")
                        },
                        onMaternalSurnameChange = { 
                            maternalSurname = it
                            clearError("maternalSurname")
                        },
                        onDocumentTypeChange = { 
                            onDocumentTypeChange(it)
                        },
                        onDocumentNumberChange = { 
                            onDocumentNumberChange(it)
                        },
                        onPhoneNumberChange = { 
                            phoneNumber = it
                            clearError("phoneNumber")
                        },
                        onEmailChange = { 
                            email = it
                            clearError("email")
                        },
                        onPasswordChange = { 
                            password = it
                            clearError("password")
                        },
                        onRepeatPasswordChange = { 
                            repeatPassword = it
                            clearError("repeatPassword")
                        },
                        onPasswordVisibilityChange = { passwordVisible = it },
                        onRepeatPasswordVisibilityChange = { repeatPasswordVisible = it },
                        onRegister = {
                            println("DEBUG: Register button clicked")
                            if (validateForm()) {
                                println("DEBUG: Validation passed, calling API")
                                // Llamar al ViewModel para registrar al usuario
                                viewModel.registerUser(
                                    names = names,
                                    paternalSurname = paternalSurname,
                                    maternalSurname = maternalSurname,
                                    documentType = documentType,
                                    documentNumber = documentNumber,
                                    phone = phoneNumber,
                                    email = email,
                                    password = password
                                )
                            } else {
                                println("DEBUG: Validation failed")
                            }
                        },
                        isLoading = state.isLoading
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    //RegisterFooter {
                    //    onNavigationBack()
                    //}
                }
            }
        }
        
        // Diálogo de confirmación de registro exitoso
        println("DEBUG: showSuccessDialog state = $showSuccessDialog")
        if (showSuccessDialog) {
            println("DEBUG: Rendering AlertDialog")
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(
                        text = "Cuenta creada",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Text(
                        text = "Ahora puede ingresar con su número de documento y contraseña para solicitar su primer servicio.",
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            onNavigationBack()
                        }
                    ) {
                        Text(
                            text = "Aceptar",
                            color = ColorPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }
                }
            )
        }
    }
}
