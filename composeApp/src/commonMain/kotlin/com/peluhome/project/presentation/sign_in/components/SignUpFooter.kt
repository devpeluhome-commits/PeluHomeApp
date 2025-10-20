package com.peluhome.project.presentation.sign_in.components

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
import com.peluhome.project.ui.ColorTextPrimary

@Composable
fun SignUpFooter(
    onNavigationNewUser:()->Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = { onNavigationNewUser() }
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    ) {
                        append("¿Aún no tienes cuenta?  ")
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Regístrate ahora")
                    }
                }
            )
        }

    }
}