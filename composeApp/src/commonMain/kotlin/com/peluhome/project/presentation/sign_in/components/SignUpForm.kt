package com.peluhome.project.presentation.sign_in.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.components.CustomButton
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
fun SignUpForm(
    user:String,
    password:String,
    visualTransformation: Boolean,
    rememberCredentials: Boolean = false,
    userError:String?=null,
    passwordError:String?=null,
    isLoading:Boolean,
    onChangeDocument:(String)->Unit,
    onChangePassword:(String)->Unit,
    onChangePasswordVisibility:(Boolean) -> Unit,
    onRememberCredentialsChange:(Boolean) -> Unit,
    onSignIn:()->Unit
) {

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextFieldComponentBasic(
            text = user,
            onValueChange = {
                onChangeDocument(it)
            },
            textLabel = "Número de documento",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            isError = false,
            leadingIcon = {
                Icon(
                    painter = painterResource(resource =  Res.drawable.icon_business_center),
                    contentDescription = "badge icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
            }
        )
        MessageValidationComponent(field = userError)



        OutlinedTextFieldComponentBasic(
            text = password,
            onValueChange = {
                onChangePassword(it)
            },
            textLabel = "Contraseña",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            isError = false,
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.icon_password),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp),
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onChangePasswordVisibility(!visualTransformation)
                    }
                ) {
                    val ic =
                        if (visualTransformation) Res.drawable.icon_invisible else Res.drawable.icon_visible
                    Icon(
                        painter = painterResource(ic),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            visualTransformation = if (visualTransformation) PasswordVisualTransformation() else VisualTransformation.None,
        )

        MessageValidationComponent(field = passwordError)

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox para recordar credenciales
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberCredentials,
                onCheckedChange = onRememberCredentialsChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorPrimary,
                    uncheckedColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recordar mis credenciales",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            label = "Iniciar sesión",
            onClick = {
                onSignIn()
            },
            isLoading = isLoading,
            color = ColorPrimary
        )
    }
}