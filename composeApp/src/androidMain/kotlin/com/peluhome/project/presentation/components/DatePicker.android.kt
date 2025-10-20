package com.peluhome.project.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.peluhome.project.ui.ColorPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun DatePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    if (show) {
        // DatePicker sin restricciones de fecha - permitir cualquier fecha
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )
        
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // El DatePicker de Material3 devuelve la fecha en UTC
                            // Necesitamos convertir correctamente a la zona horaria local
                            val utcCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                            utcCalendar.timeInMillis = millis
                            
                            // Obtener los valores de fecha en UTC
                            val day = utcCalendar.get(Calendar.DAY_OF_MONTH)
                            val month = utcCalendar.get(Calendar.MONTH) + 1 // Months are 0-based
                            val year = utcCalendar.get(Calendar.YEAR)
                            
                            val formattedDate = String.format("%02d/%02d/%04d", day, month, year)
                            onDateSelected(formattedDate)
                        }
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
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

