package com.peluhome.project.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peluhome.project.presentation.home.components.CartBottomSheet
import com.peluhome.project.presentation.home.screens.MyProfileScreen
import com.peluhome.project.presentation.home.screens.MyRequestsScreen
import com.peluhome.project.presentation.home.screens.ServicesScreen
import com.peluhome.project.presentation.history.BookingsScreen
import com.peluhome.project.ui.ColorPrimary
import org.jetbrains.compose.resources.painterResource
import peluhome.composeapp.generated.resources.Res
import peluhome.composeapp.generated.resources.icon_business_center
import peluhome.composeapp.generated.resources.icon_cart

import peluhome.composeapp.generated.resources.icon_menu
import peluhome.composeapp.generated.resources.icon_plus
import peluhome.composeapp.generated.resources.icon_profile
import peluhome.composeapp.generated.resources.icon_services
import peluhome.composeapp.generated.resources.logo_peluhome

@Composable
private fun CartIconWithBadge(
    hasItems: Boolean,
    onClick: () -> Unit
) {
    Box {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(Res.drawable.icon_cart),
                contentDescription = "Ver Carrito",
                modifier = Modifier.size(24.dp),
                tint = Color.White
            )
        }
        
        // Badge rojo si hay servicios
        if (hasItems) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    )
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    logOut: () -> Unit,
    initialTab: Int = 1 // Permite iniciar en un tab específico (por defecto Servicios)
) {
    var selectedTab by remember { mutableStateOf(initialTab) }
    
    // Estados para los diálogos
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showCartSheet by remember { mutableStateOf(false) }
    
    // Estados para el carrito (se pasarán desde ServicesScreen)
    var selectedServicesMap by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var onCartQuantityChange by remember { mutableStateOf<((Int, Int) -> Unit)?>(null) }
    var availableServices by remember { mutableStateOf<List<com.peluhome.project.domain.model.Service>>(emptyList()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo_peluhome),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .height(32.dp),
                            contentScale = ContentScale.FillHeight,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ColorPrimary,
                    titleContentColor = ColorPrimary
                ),
                navigationIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.icon_menu),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp),
                        tint = Color.White
                    )
                },
                actions = {
                    // Mostrar botón de carrito solo en step 1 y 2 de servicios
                    if (selectedTab == 1) {
                        CartIconWithBadge(
                            hasItems = selectedServicesMap.isNotEmpty(),
                            onClick = { showCartSheet = true }
                        )
                    }
                    
                    // Botón de cerrar sesión siempre visible
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_business_center),
                            contentDescription = "Cerrar Sesión",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.background(Color.White.copy(alpha = 0.95f))
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = ColorPrimary
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_profile),
                            contentDescription = "Mi Perfil",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = "Mi Perfil",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrimary,
                        selectedTextColor = ColorPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = ColorPrimary.copy(alpha = 0.1f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_services),
                            contentDescription = "Servicios",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = "Servicios",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrimary,
                        selectedTextColor = ColorPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = ColorPrimary.copy(alpha = 0.1f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            painter = painterResource(Res.drawable.icon_cart),
                            contentDescription = "Mis Solicitudes",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    },
                    label = {
                        Text(
                            text = "Mis Solicitudes",
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorPrimary,
                        selectedTextColor = ColorPrimary,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = ColorPrimary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> MyProfileScreen()
                1 -> ServicesScreen(
                    availableServices = availableServices,
                    onNavigateToRequests = {
                        selectedTab = 2 // Navegar a Mis Solicitudes
                    },
                    onCartUpdated = { servicesMap ->
                        println("DEBUG HomeScreen: Recibiendo carrito con ${servicesMap.size} servicios: $servicesMap")
                        selectedServicesMap = servicesMap
                    },
                    onCartQuantityChange = { quantityChangeFunction ->
                        onCartQuantityChange = quantityChangeFunction
                    },
                    onServicesListUpdated = { services ->
                        println("DEBUG HomeScreen: Recibiendo lista de servicios: ${services.size}")
                        // Acumular servicios de todas las categorías sin duplicados
                        val existingIds = availableServices.map { it.id }.toSet()
                        val newServices = services.filter { it.id !in existingIds }
                        availableServices = availableServices + newServices
                        println("DEBUG HomeScreen: Total servicios acumulados: ${availableServices.size}")
                    },
                    onServiceCompleted = {
                        // Resetear el carrito y servicios disponibles cuando se complete el servicio
                        selectedServicesMap = emptyMap()
                        availableServices = emptyList()
                        println("DEBUG HomeScreen: Carrito y servicios disponibles reseteados después de completar servicio")
                    }
                )
                2 -> BookingsScreen()
            }
        }
        
        // Diálogo de confirmación para cerrar sesión
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro que deseas cerrar sesión?",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            logOut()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
                    ) {
                        Text(
                            text = "Sí",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text(
                            text = "No",
                            color = ColorPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
        
        // Sheet del carrito
        if (showCartSheet) {
            CartBottomSheet(
                selectedServicesMap = selectedServicesMap,
                availableServices = availableServices,
                onDismiss = { showCartSheet = false },
                onQuantityChange = onCartQuantityChange ?: { serviceId, quantity ->
                    println("DEBUG HomeScreen: Cambio de cantidad - ServiceId: $serviceId, Quantity: $quantity")
                }
            )
        }
    }
}

