package com.peluhome.project.presentation.components

import androidx.compose.runtime.Composable

/**
 * DatePicker nativo para Android e iOS
 * Retorna la fecha en formato DD/MM/YYYY
 */
@Composable
expect fun DatePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
)

