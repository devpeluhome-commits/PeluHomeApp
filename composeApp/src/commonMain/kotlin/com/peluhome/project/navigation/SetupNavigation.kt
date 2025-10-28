package com.peluhome.project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peluhome.project.presentation.sign_in.SignInScreen
import com.peluhome.project.presentation.register_user.RegisterUserScreen
import com.peluhome.project.presentation.home.HomeScreen
import com.peluhome.project.presentation.admin.AdminBookingsScreen

@Composable
fun SetupNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SignIn
    ){
        composable<SignIn> {
            SignInScreen(
                onNavigationNewUser = {
                    navController.navigate(RegisterUser)
                },
                onNavigationHome = {
                    navController.navigate(Home)
                },
                onNavigationAdmin = {
                    navController.navigate(Admin)
                }
            )
        }
        composable<Home> {
            HomeScreen(
                logOut = {
                    navController.navigate(SignIn) {
                        popUpTo(SignIn) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<RegisterUser> {
            RegisterUserScreen(
                onNavigationBack = {
                    navController.popBackStack()
                },
                onNavigationHome = {
                    navController.navigate(Home)
                }
            )
        }
        composable<Admin> {
            AdminBookingsScreen(
                onLogout = {
                    navController.navigate(SignIn) {
                        popUpTo(SignIn) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}