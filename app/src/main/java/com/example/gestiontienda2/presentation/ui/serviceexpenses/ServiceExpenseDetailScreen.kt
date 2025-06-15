package com.example.gestiontienda2.presentation.ui.serviceexpenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.presentation.ui.components.DatePickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    // State for date picker dialog in edit mode
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    var editedDescription by remember { mutableStateOf("") }
    var editedDate by remember { mutableStateOf(System.currentTimeMillis()) } // Use Long instead of String
    var editedAmount by remember { mutableStateOf("") }
    var editedCategory by remember { mutableStateOf("") }
    var editedNotes by remember { mutableStateOf("") }

    LaunchedEffect(serviceExpenseState) {
        serviceExpenseState?.let {
            editedDescription = it.description
            editedDate = it.date
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
                                coroutineScope.launch {
                                    val updatedExpense = serviceExpenseState!!.copy(
                                        description = editedDescription,
                                        date = editedDate,
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
                                showDeleteConfirmation = true
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
            modifier = Modifier
                .padding(paddingValues)
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (editMode) {
                            OutlinedTextField(
                                value = editedDescription,
                                onValueChange = { editedDescription = it },
                                label = { Text("Description") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = dateFormat.format(Date(editedDate)),
                                    onValueChange = {},
                                    label = { Text("Date") },
                                    readOnly = true,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { showDatePickerDialog = true }) {
                                    Icon(
                                        Icons.Default.DateRange,
                                        contentDescription = "Select Date"
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = editedAmount,
                                onValueChange = { editedAmount = it },
                                label = { Text("Amount") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = editedCategory,
                                onValueChange = { editedCategory = it },
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = editedNotes,
                                onValueChange = { editedNotes = it },
                                label = { Text("Notes") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                singleLine = false
                            )
                        } else {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = 4.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Description: ${serviceExpense.description}",
                                        style = MaterialTheme.typography.body1
                                    )
                                    Text(
                                        text = "Date: ${dateFormat.format(Date(serviceExpense.date))}",
                                        style = MaterialTheme.typography.body1
                                    )
                                    Text(
                                        text = "Amount: $${
                                            String.format(
                                                "%.2f",
                                                serviceExpense.amount
                                            )
                                        }",
                                        style = MaterialTheme.typography.body1
                                    )
                                    Text(
                                        text = "Category: ${serviceExpense.category}",
                                        style = MaterialTheme.typography.body1
                                    )
                                    serviceExpense.notes?.let {
                                        Text(
                                            text = "Notes: $it",
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            }
                        }

                        if (savingState) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                        navigateBack()
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Date Picker Dialog for edit mode
        if (showDatePickerDialog) {
            DatePickerDialog(
                onDateSelected = { timestamp ->
                    editedDate = timestamp
                    showDatePickerDialog = false
                },
                onDismiss = { showDatePickerDialog = false }
            )
        }
    }
}

