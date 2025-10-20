package com.peluhome.project.presentation.register_user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.peluhome.project.presentation.components.CustomButton
import com.peluhome.project.presentation.components.DropdownComponent
import com.peluhome.project.presentation.components.MessageValidationComponent
import com.peluhome.project.presentation.components.OutlinedTextFieldComponentBasic
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_business_center
import peluhome.composeapp.generated.resources.icon_invisible
import peluhome.composeapp.generated.resources.icon_password
import peluhome.composeapp.generated.resources.icon_visible

@Composable
fun RegisterForm(
    names: String,
    paternalSurname: String,
    maternalSurname: String,
    documentType: String,
    documentNumber: String,
    phoneNumber: String,
    email: String,
    password: String,
    repeatPassword: String,
    passwordVisible: Boolean,
    repeatPasswordVisible: Boolean,
    namesError: String? = null,
    paternalSurnameError: String? = null,
    maternalSurnameError: String? = null,
    documentTypeError: String? = null,
    documentNumberError: String? = null,
    phoneNumberError: String? = null,
    emailError: String? = null,
    passwordError: String? = null,
    repeatPasswordError: String? = null,
    isLoading: Boolean,
    onNamesChange: (String) -> Unit,
    onPaternalSurnameChange: (String) -> Unit,
    onMaternalSurnameChange: (String) -> Unit,
    onDocumentTypeChange: (String) -> Unit,
    onDocumentNumberChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onRepeatPasswordVisibilityChange: (Boolean) -> Unit,
    onRegister: () -> Unit
) {

    val focusManager = LocalFocusManager.current
    val namesFocusRequester = remember { FocusRequester() }
    
    // Efecto para poner el foco inicial en el campo de nombres
    LaunchedEffect(Unit) {
        namesFocusRequester.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Campo Nombres
        OutlinedTextFieldComponentBasic(
            text = names,
            onValueChange = onNamesChange,
            textLabel = "Nombres",
            modifier = Modifier.focusRequester(namesFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = namesError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "person icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = namesError)

        // Campo Apellido Paterno
        OutlinedTextFieldComponentBasic(
            text = paternalSurname,
            onValueChange = onPaternalSurnameChange,
            textLabel = "Apellido Paterno",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = paternalSurnameError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "person icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = paternalSurnameError)

        // Campo Apellido Materno
        OutlinedTextFieldComponentBasic(
            text = maternalSurname,
            onValueChange = onMaternalSurnameChange,
            textLabel = "Apellido Materno",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = maternalSurnameError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "person icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = maternalSurnameError)

        // Campo Tipo de Documento
        DropdownComponent(
            text = documentType,
            onValueChange = onDocumentTypeChange,
            textLabel = "Tipo de Documento",
            options = listOf("DNI", "RUC", "Carnet de Extranjería"),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "document type icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = documentTypeError)

        // Campo Número de Documento
        OutlinedTextFieldComponentBasic(
            text = documentNumber,
            onValueChange = onDocumentNumberChange,
            textLabel = "Número de Documento",
            keyboardOptions = KeyboardOptions(
                keyboardType = if (documentType == "DNI" || documentType == "RUC") KeyboardType.Number else KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = documentNumberError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "document icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = documentNumberError)

        // Campo Número de Celular
        OutlinedTextFieldComponentBasic(
            text = phoneNumber,
            onValueChange = onPhoneNumberChange,
            textLabel = "Número de Celular",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = phoneNumberError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "phone icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = phoneNumberError)

        // Campo Correo
        OutlinedTextFieldComponentBasic(
            text = email,
            onValueChange = onEmailChange,
            textLabel = "Correo Electrónico",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = emailError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_business_center),
                    contentDescription = "email icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = emailError)

        // Campo Contraseña
        OutlinedTextFieldComponentBasic(
            text = password,
            onValueChange = onPasswordChange,
            textLabel = "Contraseña",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = passwordError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_password),
                    contentDescription = "password icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onPasswordVisibilityChange(!passwordVisible)
                    }
                ) {
                    val icon = if (passwordVisible) Res.drawable.icon_invisible else Res.drawable.icon_visible
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        )
        MessageValidationComponent(field = passwordError)

        // Campo Repetir Contraseña
        OutlinedTextFieldComponentBasic(
            text = repeatPassword,
            onValueChange = onRepeatPasswordChange,
            textLabel = "Repetir Contraseña",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            isError = repeatPasswordError != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_password),
                    contentDescription = "password icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onRepeatPasswordVisibilityChange(!repeatPasswordVisible)
                    }
                ) {
                    val icon = if (repeatPasswordVisible) Res.drawable.icon_invisible else Res.drawable.icon_visible
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (repeatPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        )
        MessageValidationComponent(field = repeatPasswordError)

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            label = "Crear cuenta",
            onClick = onRegister,
            isLoading = isLoading,
            color = ColorPrimary
        )
    }
}
