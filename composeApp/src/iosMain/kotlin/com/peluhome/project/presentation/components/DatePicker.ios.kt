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
                
                // Crear un formateador de fecha para evitar problemas de zona horaria
                val dateFormatter = platform.Foundation.NSDateFormatter()
                dateFormatter.dateFormat = "dd/MM/yyyy"
                dateFormatter.timeZone = platform.Foundation.NSTimeZone.systemTimeZone
                
                val formattedDate = dateFormatter.stringFromDate(selectedDate)
                
                // Debug para verificar los valores
                println("iOS DatePicker - Selected date: $selectedDate")
                println("iOS DatePicker - Formatted date: $formattedDate")
                
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

