package com.peluhome.project.presentation.register_user.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RegisterHeader() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Crear cuenta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Text(
            text = "Completa los datos para crear tu cuenta y comenzar a solicitar servicios.",
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 16.sp
        )
    }
}
