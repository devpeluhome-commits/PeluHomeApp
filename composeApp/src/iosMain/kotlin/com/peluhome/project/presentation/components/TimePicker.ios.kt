package com.peluhome.project.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCalendar
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
actual fun TimePickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    LaunchedEffect(show) {
        if (show) {
            val alertController = UIAlertController.alertControllerWithTitle(
                title = "Seleccionar Hora",
                message = "\n\n\n\n\n\n\n\n",
                preferredStyle = UIAlertControllerStyleAlert
            )
            
            val timePicker = UIDatePicker().apply {
                datePickerMode = UIDatePickerMode.UIDatePickerModeTime
                preferredDatePickerStyle = platform.UIKit.UIDatePickerStyle.UIDatePickerStyleWheels
            }
            
            alertController.view.addSubview(timePicker)
            
            // Acción OK
            val okAction = UIAlertAction.actionWithTitle(
                title = "OK",
                style = UIAlertActionStyleDefault
            ) {
                val selectedDate = timePicker.date
                val calendar = NSCalendar.currentCalendar()
                val components = calendar.components(
                    unitFlags = (platform.Foundation.NSCalendarUnitHour or 
                                platform.Foundation.NSCalendarUnitMinute).toULong(),
                    fromDate = selectedDate
                )
                
                val hour = components.hour
                val minute = components.minute
                
                val formattedTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                onTimeSelected(formattedTime)
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

