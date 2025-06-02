package com.your_app_name.presentation.ui.serviceexpenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.domain.models.ServiceExpense
import com.your_app_name.presentation.ui.common.SavingState
import com.your_app_name.presentation.ui.components.DatePickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddServiceExpenseScreen(
    viewModel: AddServiceExpenseViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val description = remember { mutableStateOf("") }
    val date = remember { mutableStateOf(System.currentTimeMillis()) }
    val amount = remember { mutableStateOf("") }
    val category = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    val savingState by viewModel.savingState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Basic date formatting for display
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // State for date picker dialog
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // Date formatting for display
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    // State for date picker dialog
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(title = { Text("Add New Service Expense") })

            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = dateFormat.format(Date(date.value)),
                    onValueChange = {}, // Read-only for display
                    label = { Text("Date") },
                    readOnly = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showDatePickerDialog = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amount.value,
                onValueChange = { amount.value = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category.value,
                onValueChange = { category.value = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp) // Make it multiline
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val expense = ServiceExpense(
                        description = description.value,
                        date = date.value,
                        amount = amount.value.toDoubleOrNull() ?: 0.0,
                        category = category.value,
                        notes = notes.value.ifBlank { null }
                    )
                    viewModel.addServiceExpense(expense)
                },
                enabled = savingState != SavingState.Saving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Service Expense")
            }

            when (savingState) {
                SavingState.Saving -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                SavingState.Success -> {
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Service Expense saved successfully!")
                            navigateBack()
                        }
                    }
                }

                is SavingState.Error -> {
                    LaunchedEffect(savingState) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Error saving Service Expense: ${(savingState as SavingState.Error).message}")
                        }
                    }
                }

                else -> {}
            }
        }
    }

    // Date Picker Dialog
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDateSelected = { timestamp ->
                date.value = timestamp
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
}

}
)
}
}