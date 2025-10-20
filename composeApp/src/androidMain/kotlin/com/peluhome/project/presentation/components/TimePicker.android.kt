package com.peluhome.project.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.peluhome.project.ui.ColorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TimePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    if (show) {
        val timePickerState = rememberTimePickerState(
            initialHour = 12,
            initialMinute = 0,
            is24Hour = true
        )
        
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute
                        val formattedTime = String.format("%02d:%02d", hour, minute)
                        onTimeSelected(formattedTime)
                        onDismiss()
                    }
                ) {
                    Text("OK", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancelar", color = Color.White)
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

