package com.example.gestiontienda2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestiontienda2.presentation.ui.auth.LoginScreen
import com.example.gestiontienda2.presentation.ui.auth.RegistrationScreen
import com.example.gestiontienda2.presentation.ui.dashboard.DashboardScreen
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme
import com.example.gestiontienda2.presentation.viewmodels.auth.LoginViewModel
import com.example.gestiontienda2.presentation.viewmodels.auth.RegistrationViewModel

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
                            val loginViewModel: LoginViewModel = hiltViewModel()
                            LoginScreen(
                                // viewModel = loginViewModel, // Assuming LoginScreen takes a ViewModel
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
                            val registrationViewModel: RegistrationViewModel = hiltViewModel()
                            RegistrationScreen(
                                // viewModel = registrationViewModel, // Assuming RegistrationScreen takes a ViewModel
                                onRegistrationSuccess = {
                                    navController.navigate(ROUTE_LOGIN) {
                                        popUpTo(ROUTE_REGISTRATION) { inclusive = true }
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
