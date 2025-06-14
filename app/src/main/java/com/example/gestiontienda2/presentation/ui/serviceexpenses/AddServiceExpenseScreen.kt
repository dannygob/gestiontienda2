package com.example.gestiontienda2.presentation.ui.serviceexpenses

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.ServiceExpense
import com.example.gestiontienda2.presentation.ui.components.DatePickerDialog
import com.example.gestiontienda2.presentation.viewmodels.SavingState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Add New Service Expense") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

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

            OutlinedTextField(
                value = amount.value,
                onValueChange = { amount.value = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category.value,
                onValueChange = { category.value = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                            scaffoldState.snackbarHostState.showSnackbar("Error saving Service Expense: ${savingState.message}")
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

@Composable
fun Scaffold(
    scaffoldState: rememberScaffoldState,
    topBar: @Composable () -> TopAppBar,
    content: @Composable (ERROR) -> Unit,
) {
    TODO("Not yet implemented")
}

@Composable
fun Button(
    onClick: () -> addServiceExpense,
    enabled: Boolean,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    TODO("Not yet implemented")
}