package com.peluhome.project.presentation.home.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.components.AlertComponent
import com.peluhome.project.presentation.profile.ProfileViewModel
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.background
import peluhome.composeapp.generated.resources.icon_business_center

// Data class para representar el perfil del usuario
data class UserProfile(
    val names: String,
    val paternalSurname: String,
    val maternalSurname: String,
    val documentType: String,
    val documentNumber: String,
    val phoneNumber: String,
    val email: String
)

@Composable
fun MyProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state = viewModel.state
    
    // Cargar perfil al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    // Mostrar error si existe
    state.error?.let { error ->
        AlertComponent(
            title = "Error",
            message = error,
            dismiss = { viewModel.clearError() },
            action = { viewModel.clearError() }
        )
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(Res.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            if (state.isLoading) {
                // Mostrar loading
                CircularProgressIndicator(
                    color = ColorPrimary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Cargando perfil...",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            } else if (state.user != null) {
                // Mostrar datos del usuario
                val user = state.user
                
                // Avatar circular con icono
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(ColorPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${user.names.first()}${user.paternalSurname.first()}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "${user.names} ${user.paternalSurname} ${user.maternalSurname}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Card con información del perfil
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Información Personal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorPrimary
                        )
                        
                        ProfileField(
                            label = "Nombres",
                            value = user.names
                        )
                        
                        ProfileField(
                            label = "Apellido Paterno",
                            value = user.paternalSurname
                        )
                        
                        ProfileField(
                            label = "Apellido Materno",
                            value = user.maternalSurname
                        )
                        
                        ProfileField(
                            label = "Tipo de Documento",
                            value = user.documentType
                        )
                        
                        ProfileField(
                            label = "Número de Documento",
                            value = user.documentNumber
                        )
                        
                        ProfileField(
                            label = "Número de Celular",
                            value = user.phone
                        )
                        
                        ProfileField(
                            label = "Correo Electrónico",
                            value = user.email
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // Mostrar mensaje de error o estado vacío
                Text(
                    text = "No se pudo cargar el perfil",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}

