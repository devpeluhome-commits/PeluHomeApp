package com.peluhome.project.presentation.sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorTextPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.logo_peluhome

@Composable
fun SignUpHeader() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier.height(80.dp).padding(8.dp),
            painter = painterResource(Res.drawable.logo_peluhome),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Bienvenido",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Text(
            text = "Ingresa tu usuario y contraseña para iniciar sesión.",
            fontSize = 14.sp,
            color = Color.Black,
            lineHeight = 16.sp
        )
    }
}