package com.peluhome.project.presentation.components

import androidx.compose.runtime.Composable

/**
 * TimePicker nativo para Android e iOS
 * Retorna la hora en formato HH:MM
 */
@Composable
expect fun TimePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
)

