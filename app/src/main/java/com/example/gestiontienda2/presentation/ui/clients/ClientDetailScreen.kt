package com.example.gestiontienda2.presentation.ui.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ClientDetailScreen(
    viewModel: ClientDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val clientState by viewModel.client.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isEditMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (isEditMode) "Edit Client" else "Client Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (clientState != null) {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(
                                imageVector = if (isEditMode) Icons.Filled.Save else Icons.Filled.Edit,
                                contentDescription = if (isEditMode) "Save" else "Edit"
                            )
                        }
                        if (isEditMode) {
                            // Save button if separate from toggle
                            IconButton(onClick = { viewModel.saveClient() }) {
                                Icon(Icons.Filled.Save, contentDescription = "Save")
                            }
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
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colors.error
                )

                clientState != null -> {
                    val client = clientState!!
                    var name by remember { mutableStateOf(client.name) }
                    var phone by remember { mutableStateOf(client.phone) }
                    var email by remember { mutableStateOf(client.email) }
                    var address by remember { mutableStateOf(client.address) }

                    LaunchedEffect(client) {
                        name = client.name
                        phone = client.phone ?: ""
                        email = client.email ?: ""
                        address = client.address ?: ""
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isEditMode) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    viewModel.updateName(it)
                                },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = phone,
                                onValueChange = {
                                    phone = it
                                    viewModel.updatePhone(it)
                                },
                                label = { Text("Phone") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    viewModel.updateEmail(it)
                                },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = address,
                                onValueChange = {
                                    address = it
                                    viewModel.updateAddress(it)
                                },
                                label = { Text("Address") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.saveClient() },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Save Client")
                            }
                        } else {
                            Text(text = "Name: ${client.name}", style = MaterialTheme.typography.h6)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Phone: ${client.phone}")
                            Text(text = "Email: ${client.email ?: "N/A"}")
                            Text(text = "Address: ${client.address}")
                            // Display other client details here
                        }
                    }
                }

                else -> {
                    // Handle case where client is null but not loading/error
                    Text("Client not found.")
                }
            }

            // Display saving indicator if needed
            if (savingState == ClientDetailViewModel.SavingState.Saving) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (savingState == ClientDetailViewModel.SavingState.Success) {
                LaunchedEffect(savingState) {
                    navigateBack()
                }
            }
            if (savingState is ClientDetailViewModel.SavingState.Error) {
                Text(
                    text = "Save Error: ${(savingState as ClientDetailViewModel.SavingState.Error).message}",
                    color = MaterialTheme.colors.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}



