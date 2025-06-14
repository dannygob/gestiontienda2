package com.example.gestiontienda2.presentation.ui.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.presentation.viewmodels.DetailedSaleState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailScreen(
    viewModel: SaleDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val detailedSaleState by viewModel.detailedSaleState.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    // Delete confirmation dialog state
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Handle navigation after successful operations
    LaunchedEffect(savingState) {
        if (savingState is SaleDetailViewModel.SavingState.Success) {
            if (editMode) {
                viewModel.toggleEditMode()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sale Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (detailedSaleState.sale != null) {
                        if (!editMode) {
                            IconButton(onClick = { viewModel.toggleEditMode() }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit Sale")
                            }
                            IconButton(onClick = { showDeleteConfirmation = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Sale")
                            }
                        } else {
                            IconButton(onClick = { viewModel.saveSale() }) {
                                Icon(Icons.Filled.Save, contentDescription = "Save Sale")
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
                .padding(16.dp)
        ) {
            when {
                detailedSaleState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                detailedSaleState.error != null -> {
                    Text(
                        "Error: ${detailedSaleState.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                detailedSaleState.sale == null -> {
                    Text(
                        "Sale not found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    SaleDetailContent(
                        detailedSaleState = detailedSaleState,
                        editMode = editMode,
                        savingState = savingState,
                        onQuantityChange = { itemId, quantity ->
                            viewModel.updateSaleItemQuantity(itemId, quantity)
                        },
                        onRemoveItem = { itemId ->
                            viewModel.removeSaleItem(itemId)
                        }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this sale?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteSale()
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
}

@Composable
fun SaleDetailContent(
    detailedSaleState: DetailedSaleState,
    editMode: Boolean,
    savingState: SaleDetailViewModel.SavingState,
    onQuantityChange: (Int, Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
) {
    val sale = detailedSaleState.sale!!
    val client = detailedSaleState.client
    val itemsWithProducts = detailedSaleState.itemsWithProducts

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Sale Details", style = MaterialTheme.typography.headlineSmall)

        // Sale Information
        Text("Sale ID: ${sale.id}")
        Text("Client: ${client?.name ?: "Unknown Client"}")
        Text(
            "Sale Date: ${
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date(sale.saleDate))
            }"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Items", style = MaterialTheme.typography.bodyLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(itemsWithProducts, key = { it.saleItem.id }) { itemWithProduct ->
                if (editMode) {
                    SaleItemEditRow(
                        item = itemWithProduct.saleItem,
                        productName = itemWithProduct.product?.name ?: "Unknown Product",
                        onQuantityChange = { quantity ->
                            onQuantityChange(itemWithProduct.saleItem.id, quantity)
                        },
                        onRemoveItem = {
                            onRemoveItem(itemWithProduct.saleItem.id)
                        }
                    )
                } else {
                    SaleItemViewRow(
                        item = itemWithProduct.saleItem,
                        productName = itemWithProduct.product?.name ?: "Unknown Product"
                    )
                }
            }
        }

        Text(
            "Total: $${String.format("%.2f", sale.totalAmount)}",
            style = MaterialTheme.typography.headlineSmall
        )

        // Saving state feedback
        when (savingState) {
            is SaleDetailViewModel.SavingState.Saving -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is SaleDetailViewModel.SavingState.Error -> {
                Text(
                    "Save Error: ${savingState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }

            is SaleDetailViewModel.SavingState.Success -> {
                Text(
                    "Sale updated successfully!",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            else -> { /* No UI needed for Idle state */
            }
        }
    }
}

@Composable
fun SaleItemViewRow(
    item: SaleItem,
    productName: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Product: $productName", style = MaterialTheme.typography.bodyMedium)
            Text("Quantity: ${item.quantity}", style = MaterialTheme.typography.bodySmall)
            Text(
                "Price at Sale: $${String.format("%.2f", item.priceAtSale)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Subtotal: $${String.format("%.2f", item.quantity * item.priceAtSale)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SaleItemEditRow(
    item: SaleItem,
    productName: String,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Product: $productName", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Price: $${String.format("%.2f", item.priceAtSale)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = item.quantity.toString(),
                    onValueChange = { newValue ->
                        val quantity = newValue.toIntOrNull() ?: 0
                        if (quantity >= 0) {
                            onQuantityChange(quantity)
                        }
                    },
                    label = { Text("Qty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(80.dp),
                    singleLine = true
                )

                IconButton(onClick = onRemoveItem) {
                    Icon(
                        Icons.Filled.RemoveCircle,
                        contentDescription = "Remove Item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}