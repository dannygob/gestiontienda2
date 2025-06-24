package com.example.gestiontienda2.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.presentation.viewmodels.auth.RegistrationViewModel

@Composable
fun RegistrationScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateBack: () -> Unit, // To navigate back to login, for example
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Example: Observe a registration result state from the ViewModel
    // val registrationState by viewModel.registrationState.collectAsState()
    // LaunchedEffect(registrationState) {
    //     if (registrationState is RegistrationResult.Success) {
    //         onRegistrationSuccess()
    //     } else if (registrationState is RegistrationResult.Error) {
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
        Text(text = "Register", style = MaterialTheme.typography.headlineMedium)
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // viewModel.register(username, password, confirmPassword) // ViewModel handles actual registration
            // For now, directly call onRegistrationSuccess for flow testing
            onRegistrationSuccess()
        }) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateBack) {
            Text("Back to Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    // Preview won't work easily with Hilt ViewModel and navigation lambdas
    RegistrationScreen(onRegistrationSuccess = {}, onNavigateBack = {})
}
