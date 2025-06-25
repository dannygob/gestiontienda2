package com.your_app_name.presentation.ui.purchases

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.clickable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.your_app_name.domain.models.Purchase
import com.your_app_name.domain.models.PurchaseItem
import com.your_app_name.presentation.viewmodels.PurchaseDetailViewModel // Assuming PurchaseDetailViewModel exists
import com.your_app_name.util.SavingState // Assuming SavingState sealed class
import com.your_app_name.presentation.ui.components.DatePickerDialog // Import DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseDetailScreen(
    viewModel: PurchaseDetailViewModel = hiltViewModel(),
    purchaseId: Int,
    navigateBack: () -> Unit
) {
    val purchaseState by viewModel.purchase.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val editMode by viewModel.editMode.collectAsStateWithLifecycle()
    val savingState by viewModel.savingState.collectAsStateWithLifecycle() // Assuming savingState is a Flow in ViewModel

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    // State for selected date timestamp (Long) in edit mode
    var editedPurchaseDateTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    // State for delete confirmation dialog visibility
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Update editedPurchaseDateTimestamp when purchaseState changes and in edit mode
    LaunchedEffect(purchaseState, editMode) {
        if (editMode && purchaseState != null) {
            editedPurchaseDateTimestamp = purchaseState!!.purchaseDate
        }
    }
    // Load the purchase when the screen is first created or purchaseId changes
    LaunchedEffect(purchaseId) {
        viewModel.loadPurchase(purchaseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!editMode) {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Purchase")
                        }
                    } else {
                        IconButton(onClick = {
                            // Pass the edited date to the ViewModel when saving
                            viewModel.savePurchase(editedPurchaseDateTimestamp)
                        }) {
                            Icon(Icons.Filled.Save, contentDescription = "Save Purchase")
                        }
                    }
                    if (!editMode) {
                        IconButton(onClick = { showDeleteConfirmation = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Purchase")
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
                loading -> CircularProgressIndicator()
                error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
                purchaseState == null -> Text("Purchase not found")
                else -> {
                    val purchase = purchaseState!!
                    if (editMode) {
                        // Edit Mode UI
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Edit Purchase", style = MaterialTheme.typography.headlineSmall)

                            // TODO: Add UI for selecting/changing provider

                            // Date Input with DatePicker
                            OutlinedTextField(
                                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(editedPurchaseDateTimestamp)),
                                onValueChange = { /* Read-only, date is selected via picker */ },
                                label = { Text("Purchase Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select Date")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true } // Make the entire field clickable
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Items", style = MaterialTheme.typography.bodyLarge)
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(purchase.items) { item ->
                                    // TODO: Create a composable for editing purchase items
                                    Text("Item: ${item.productId} - Qty: ${item.quantity} - Price: ${item.priceAtPurchase}")
                                }
                            }

                            // TODO: Add Button to add new items to the purchase

                            Text("Total: ${purchase.totalAmount}", style = MaterialTheme.typography.headlineSmall)

                            when (savingState) {
                                is SavingState.Saving -> CircularProgressIndicator()
                                is SavingState.Error -> Text("Save Error: ${(savingState as SavingState.Error).message}", color = MaterialTheme.colorScheme.error)
                                SavingState.Success -> {
                                    // Optionally show a success message briefly or rely on navigation
                                }
                                else -> {}
                            }
                        }
                    } else {
                        // View Mode UI
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Purchase Details", style = MaterialTheme.typography.headlineSmall)
                            Text("Purchase ID: ${purchase.id}")
                            Text("Provider ID: ${purchase.providerId}") // TODO: Display Provider Name
                            Text("Purchase Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(purchase.purchaseDate))}") // Format date

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Items", style = MaterialTheme.typography.bodyLarge)
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(purchase.items) { item ->
                                    // TODO: Create a composable for viewing purchase items
                                    Text("Item: ${item.productId} - Qty: ${item.quantity} - Price: ${item.priceAtPurchase}")
                                }
                            }

                            Text("Total: ${purchase.totalAmount}", style = MaterialTheme.typography.headlineSmall)
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
                text = { Text("Are you sure you want to delete this purchase?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.deletePurchase() // Call delete function
                        showDeleteConfirmation = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = { Button(onClick = { showDeleteConfirmation = false }) { Text("Cancel") } })
        }
    }
}

// DatePickerDialog
@Composable
fun PurchaseDetailScreen_DatePickerDialog(
    showDatePicker: Boolean,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDatePicker) {
        DatePickerDialog(onDateSelected = onDateSelected, onDismiss = onDismiss)
    }
}

// TODO: Create PurchaseItemViewRow and PurchaseItemEditRow composables similar to Sale items