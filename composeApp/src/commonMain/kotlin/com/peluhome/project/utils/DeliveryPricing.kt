package com.peluhome.project.utils

import kotlinx.datetime.*

object DeliveryPricing {
    /**
     * Calcula el costo de delivery según la fecha y hora
     * - Días domingos y feriados: S/ 15
     * - Días normales después de las 6:00 PM: S/ 10
     * - Días normales antes de las 6:00 PM: S/ 5
     */
    fun calculateDeliveryCost(dateString: String, timeString: String): Double {
        try {
            // Parse date (formato DD/MM/YYYY)
            val dateParts = dateString.split("/")
            if (dateParts.size != 3) return 5.0
            
            val day = dateParts[0].toIntOrNull() ?: return 5.0
            val month = dateParts[1].toIntOrNull() ?: return 5.0
            val year = dateParts[2].toIntOrNull() ?: return 5.0
            
            // Parse time (formato HH:MM)
            val timeParts = timeString.split(":")
            if (timeParts.size != 2) return 5.0
            
            val hour = timeParts[0].toIntOrNull() ?: return 5.0
            
            // Crear LocalDate para verificar el día de la semana
            val localDate = LocalDate(year, month, day)
            val dayOfWeek = localDate.dayOfWeek
            
            // Regla 1: Domingos
            if (dayOfWeek == DayOfWeek.SUNDAY) {
                return 15.0
            }
            
            // Regla 2: Después de las 6:00 PM (18:00)
            if (hour >= 18) {
                return 10.0
            }
            
            // Regla 3: Días normales antes de las 6:00 PM
            return 5.0
            
        } catch (e: Exception) {
            // En caso de error al parsear, retornar el mínimo
            return 5.0
        }
    }
    
    /**
     * Retorna una descripción del delivery según las reglas de precios
     */
    fun getDeliveryDescription(dateString: String, timeString: String): String {
        val cost = calculateDeliveryCost(dateString, timeString)
        
        return when (cost) {
            15.0 -> "Delivery (Domingo/Feriado)"
            10.0 -> "Delivery (Después de 6 PM)"
            else -> "Delivery"
        }
    }
}
