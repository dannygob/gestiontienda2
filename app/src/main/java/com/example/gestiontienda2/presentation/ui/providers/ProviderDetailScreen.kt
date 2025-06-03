package com.example.gestiontienda2.presentation.ui.providers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestiontienda2.presentation.ui.providers.ProviderDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderDetailScreen(
    viewModel: ProviderDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val providerState by viewModel.provider.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState() // Assuming savingState is exposed

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Provider Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isLoading && errorMessage == null && providerState != null) {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(
                                if (editMode) Icons.Filled.Save else Icons.Filled.Edit,
                                contentDescription = if (editMode) "Save Provider" else "Edit Provider"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage != null -> {
                    Text(text = errorMessage!!, color = MaterialTheme.colors.error)
                }

                providerState != null -> {
                    val provider = providerState!!
                    if (editMode) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = provider.name,
                                onValueChange = { viewModel.updateName(it) },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = provider.phone,
                                onValueChange = { viewModel.updatePhone(it) },
                                label = { Text("Phone") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = provider.email,
                                onValueChange = { viewModel.updateEmail(it) },
                                label = { Text("Email") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = provider.address,
                                onValueChange = { viewModel.updateAddress(it) },
                                label = { Text("Address") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.saveProvider() },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = savingState != SavingState.Saving // Assuming SavingState sealed class
                            ) {
                                Text("Save Changes")
                            }

                            if (savingState == SavingState.Saving) { // Assuming SavingState sealed class
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            }
                            // Display saving error if needed
                            if (savingState is SavingState.Error) { // Assuming SavingState sealed class
                                Text(
                                    text = (savingState as SavingState.Error).message,
                                    color = MaterialTheme.colors.error
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Name: ${provider.name}",
                                style = MaterialTheme.typography.h6
                            )
                            Text(text = "Phone: ${provider.phone}")
                            Text(text = "Email: ${provider.email}")
                            Text(text = "Address: ${provider.address}")
                        }
                    }
                }
            }
        }
    }
}

// Assuming a sealed class to represent saving state
sealed class SavingState {
    object Idle : SavingState()
    object Saving : SavingState()
    object Success : SavingState()
    data class Error(val message: String) : SavingState()
}