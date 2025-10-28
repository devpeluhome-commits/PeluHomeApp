package com.peluhome.project.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun DatePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val alertController = UIAlertController.alertControllerWithTitle(
                title = "Seleccionar Fecha",
                message = "\n\n\n\n\n\n\n\n",
                preferredStyle = UIAlertControllerStyleAlert
            )
            
            val datePicker = UIDatePicker().apply {
                datePickerMode = UIDatePickerMode.UIDatePickerModeDate
                preferredDatePickerStyle = platform.UIKit.UIDatePickerStyle.UIDatePickerStyleWheels
                // Establecer la fecha actual como fecha inicial
                date = platform.Foundation.NSDate()
                // Sin restricciones de fecha - permitir cualquier fecha
                // La validación de fechas pasadas se hace en el Paso 3
            }
            
            alertController.view.addSubview(datePicker)
            
            // Acción OK
            val okAction = UIAlertAction.actionWithTitle(
                title = "OK",
                style = UIAlertActionStyleDefault
            ) {
                val selectedDate = datePicker.date
                
                // Usar calendario por defecto (ya maneja la zona horaria local)
                val calendar = NSCalendar.currentCalendar()
                
                val components = calendar.components(
                    unitFlags = (platform.Foundation.NSCalendarUnitDay or 
                                platform.Foundation.NSCalendarUnitMonth or 
                                platform.Foundation.NSCalendarUnitYear).toULong(),
                    fromDate = selectedDate
                )
                
                val day = components.day
                val month = components.month
                val year = components.year
                
                // Debug para verificar los valores
                println("iOS DatePicker - Selected date: $selectedDate")
                println("iOS DatePicker - Day: $day, Month: $month, Year: $year")
                
                val formattedDate = "${day.toString().padStart(2, '0')}/${month.toString().padStart(2, '0')}/$year"
                onDateSelected(formattedDate)
                onDismiss()
            }
            
            // Acción Cancelar
            val cancelAction = UIAlertAction.actionWithTitle(
                title = "Cancelar",
                style = UIAlertActionStyleCancel
            ) {
                onDismiss()
            }
            
            alertController.addAction(okAction)
            alertController.addAction(cancelAction)
            
            // Mostrar el alert
            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(
                alertController,
                animated = true,
                completion = null
            )
        }
    }
}

