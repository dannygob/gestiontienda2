package com.example.gestiontienda2.presentation.ui.serviceexpenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.protolayout.modifiers.padding
import kotlinx.coroutines.launch

@Composable
fun ServiceExpenseDetailScreen(
    viewModel: ServiceExpenseDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val serviceExpenseState by viewModel.serviceExpense.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    // State for showing delete confirmation dialog
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var editedDescription by remember { mutableStateOf("") }
    var editedDate by remember { mutableStateOf("") } // Consider a proper DatePicker
    var editedAmount by remember { mutableStateOf("") }
    var editedCategory by remember { mutableStateOf("") }
    var editedNotes by remember { mutableStateOf("") }

    LaunchedEffect(serviceExpenseState) {
        serviceExpenseState?.let {
            editedDescription = it.description
            editedDate = it.date.toString() // Convert Long date to String for TextField
            editedAmount = it.amount.toString()
            editedCategory = it.category
            editedNotes = it.notes ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Expense Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (serviceExpenseState != null) {
                        if (editMode) {
                            IconButton(onClick = {
                                // TODO: Implement save logic in ViewModel
                                coroutineScope.launch {
                                    val updatedExpense = serviceExpenseState!!.copy(
                                        description = editedDescription,
                                        date = editedDate.toLongOrNull()
                                            ?: 0L, // Convert back to Long
                                        amount = editedAmount.toDoubleOrNull() ?: 0.0,
                                        category = editedCategory,
                                        notes = editedNotes.ifBlank { null }
                                    )
                                    viewModel.updateServiceExpense(updatedExpense)
                                }
                            }) {
                                Icon(Icons.Default.Save, contentDescription = "Save")
                            }
                        } else {
                            IconButton(onClick = { viewModel.toggleEditMode() }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = {
                                showDeleteConfirmation = true // Show confirmation dialog
//                                // TODO: Implement delete confirmation and logic in ViewModel
//                                coroutineScope.launch {
//                                   serviceExpenseState?.let { viewModel.deleteServiceExpense(it) }
//                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text("Error: $error")
                }

                serviceExpenseState != null -> {
                    val serviceExpense = serviceExpenseState!!
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (editMode) {
                            TextField(
                                value = editedDescription,
                                onValueChange = { editedDescription = it },
                                label = { Text("Description") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedDate,
                                onValueChange = { editedDate = it },
                                label = { Text("Date (YYYYMMDD)") }, // Indicate expected format
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Basic validation hint
                                // TODO: Replace with a proper DatePicker implementation
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedAmount,
                                onValueChange = { editedAmount = it },
                                label = { Text("Amount") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedCategory,
                                onValueChange = { editedCategory = it },
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedNotes,
                                onValueChange = { editedNotes = it },
                                label = { Text("Notes") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                singleLine = false,
                                maxLines = 5,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),

                            )
                        } else {
                            Text("Description: ${serviceExpense.description}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Date: ${serviceExpense.date}") // Format date nicely
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Amount: ${serviceExpense.amount}") // Format currency
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Category: ${serviceExpense.category}")
                            Spacer(modifier = Modifier.height(8.dp))
                            serviceExpense.notes?.let {
                                Text("Notes: $it")
                            }
                        }

                        if (savingState) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        // Observe savingState for success and navigate back
                        LaunchedEffect(savingState) {
                            if (savingState == false && error == null && !editMode && serviceExpenseState != null) {
                                // Assuming savingState becomes false after successful save and editMode is turned off
                                // and no error occurred. This is a simplified navigation trigger.
                                // You might need a dedicated event or state in ViewModel for navigation after save/delete.
                                // For now, just navigate back after successful save/delete.
                                // This might not be ideal if you want to stay on the detail screen after editing.
                                // Adjust based on desired user flow.
                                // If deleting, ViewModel should handle navigation after successful deletion.
                                // For simplicity, we navigate back after any save or delete attempt here.
                                if (serviceExpenseState == null) { // Assuming null serviceExpenseState means it was deleted
                                    navigateBack()
                                }
                            }
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Confirm Deletion") },
                text = { Text("Are you sure you want to delete this service expense?") },
                confirmButton = {
                    Button(onClick = {
                        serviceExpenseState?.let { viewModel.deleteServiceExpense(it) }
                        showDeleteConfirmation = false
                    }) { Text("Delete") }
                },
                dismissButton = {
                    Button(onClick = {
                        showDeleteConfirmation = false
                    }) { Text("Cancel") }
                }
            )
        }
    }
}

// You would need to add Previews for this Composable
// @Preview(showBackground = true)
// @Composable
// fun PreviewServiceExpenseDetailScreen() {
//     // Create mock ViewModel and ServiceExpense data for preview
// }