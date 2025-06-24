package com.example.gestiontienda2.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.presentation.viewmodels.auth.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Example: Observe a login result state from the ViewModel
    // val loginState by viewModel.loginState.collectAsState()
    // LaunchedEffect(loginState) {
    //     if (loginState is LoginResult.Success) {
    //         onLoginSuccess()
    //     } else if (loginState is LoginResult.Error) {
    //         // Show error message
    //     }
    // }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // viewModel.login(username, password) // ViewModel would handle actual login
            // For now, directly call onLoginSuccess for flow testing
            onLoginSuccess()
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text("Register")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Preview won't work easily with Hilt ViewModel and navigation lambdas
    // For a simple preview, you might need to pass mock lambdas
    LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
}
