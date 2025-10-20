package com.peluhome.project.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
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
        // Configurar la fecha mínima como hoy
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            // Establecer fecha mínima como hoy
            selectableDates = object : androidx.compose.material3.SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Convertir a UTC y comparar solo fechas (sin hora)
                    val selectedCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                    selectedCalendar.timeInMillis = utcTimeMillis
                    
                    val todayCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                    todayCalendar.set(Calendar.HOUR_OF_DAY, 0)
                    todayCalendar.set(Calendar.MINUTE, 0)
                    todayCalendar.set(Calendar.SECOND, 0)
                    todayCalendar.set(Calendar.MILLISECOND, 0)
                    
                    return selectedCalendar.timeInMillis >= todayCalendar.timeInMillis
                }
            }
        )
        
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Usar UTC para evitar problemas de zona horaria
                            val utcCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                            utcCalendar.timeInMillis = millis
                            
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

