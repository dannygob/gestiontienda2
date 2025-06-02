package com.your_app_name.presentation.ui.providers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddProviderScreen(
    viewModel: AddProviderViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val email by viewModel.email.collectAsState()
    val address by viewModel.address.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Provider") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::updateName,
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = viewModel::updatePhone,
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = viewModel::updateEmail,
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address,
                onValueChange = viewModel::updateAddress,
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            when (savingState) {
                SavingState.Idle -> {
                    Button(
                        onClick = viewModel::saveProvider,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Provider")
                    }
                }

                SavingState.Saving -> {
                    CircularProgressIndicator()
                }

                SavingState.Success -> {
                    Text("Provider Saved Successfully!")
                    // Optionally, trigger navigation back here or let the ViewModel handle it
                    // In this example, navigateBack is called from the ViewModel
                }

                is SavingState.Error -> {
                    Text(
                        "Error saving provider: ${savingState.message}",
                        color = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}