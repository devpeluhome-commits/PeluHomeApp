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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
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
fun MyProfileScreen() {
    // Datos ficticios del usuario
    val userProfile = UserProfile(
        names = "Juan Carlos",
        paternalSurname = "Pérez",
        maternalSurname = "García",
        documentType = "DNI",
        documentNumber = "12345678",
        phoneNumber = "987654321",
        email = "juan.perez@email.com"
    )
    
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
            
            // Avatar circular con icono
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(ColorPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${userProfile.names.first()}${userProfile.paternalSurname.first()}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "${userProfile.names} ${userProfile.paternalSurname} ${userProfile.maternalSurname}",
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
                        value = userProfile.names
                    )
                    
                    ProfileField(
                        label = "Apellido Paterno",
                        value = userProfile.paternalSurname
                    )
                    
                    ProfileField(
                        label = "Apellido Materno",
                        value = userProfile.maternalSurname
                    )
                    
                    ProfileField(
                        label = "Tipo de Documento",
                        value = userProfile.documentType
                    )
                    
                    ProfileField(
                        label = "Número de Documento",
                        value = userProfile.documentNumber
                    )
                    
                    ProfileField(
                        label = "Número de Celular",
                        value = userProfile.phoneNumber
                    )
                    
                    ProfileField(
                        label = "Correo Electrónico",
                        value = userProfile.email
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
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

