package com.peluhome.project.presentation.register_user.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorPrimary

@Composable
fun RegisterFooter(
    onNavigationBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = { onNavigationBack() }
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    ) {
                        append("¿Ya tienes cuenta?  ")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Inicia sesión")
                    }
                }
            )
        }
    }
}





