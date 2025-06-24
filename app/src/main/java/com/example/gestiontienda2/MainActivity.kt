package com.example.gestiontienda2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestiontienda2.presentation.ui.auth.LoginScreen
import com.example.gestiontienda2.presentation.ui.auth.RegistrationScreen
import com.example.gestiontienda2.presentation.ui.dashboard.DashboardScreen
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme

// Constantes de ruta
const val ROUTE_LOGIN = "login"
const val ROUTE_REGISTRATION = "registration"
const val ROUTE_MAIN_APP = "main_app" // Marcador para después del inicio de sesión

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionTiendaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = ROUTE_LOGIN) {
                        composable(ROUTE_LOGIN) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(ROUTE_MAIN_APP) {
                                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = {
                                    navController.navigate(ROUTE_REGISTRATION)
                                }
                            )
                        }
                        composable(ROUTE_REGISTRATION) {
                            RegistrationScreen(
                                onRegistrationSuccess = {
                                    navController.navigate(ROUTE_LOGIN) {
                                        popUpTo(ROUTE_REGISTRATION) { inclusive = true } // Go to Login, remove registration from backstack
                                    }
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(ROUTE_MAIN_APP) {
                            DashboardScreen(
                                onNavigateToProducts = { /* TODO: navController.navigate("products_route") */ },
                                onNavigateToClients = { /* TODO: navController.navigate("clients_route") */ },
                                onNavigateToProviders = { /* TODO: navController.navigate("providers_route") */ }
                                // TODO: Add logout functionality, e.g.,
                                // onLogout = {
                                //     navController.navigate(ROUTE_LOGIN) {
                                //         popUpTo(ROUTE_MAIN_APP) { inclusive = true }
                                //     }
                                // }
                            )
                        }
                    }
                }
            }
        }
    }
}
