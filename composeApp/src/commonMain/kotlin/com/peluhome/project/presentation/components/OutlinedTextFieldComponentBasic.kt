package com.peluhome.project.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.BackgroundTextField
import com.peluhome.project.ui.BorderTextField
import com.peluhome.project.ui.ColorTextField

@Composable
fun OutlinedTextFieldComponentBasic(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    textLabel: String,
    roundedDp: Dp = 16.dp,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean,
    readOnly: Boolean = false,
    enabled: Boolean = true,
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        label = {
            Text(
                text = textLabel,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = ColorTextField
            )
        },
        shape = RoundedCornerShape(roundedDp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = BorderTextField,
            focusedBorderColor = BorderTextField,
            unfocusedContainerColor = BackgroundTextField,
            focusedContainerColor = BackgroundTextField,

            ),
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        isError = isError,
        readOnly = readOnly,
        enabled = enabled
    )
}