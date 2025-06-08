package com.example.gestiontienda2.presentation.ui.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.SaleItem
import com.example.gestiontienda2.presentation.viewmodels.SaleDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailScreen(
    viewModel: SaleDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val editMode by viewModel.editMode.collectAsState()
    val detailedSaleState by viewModel.detailedSaleState.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    val sale = detailedSaleState.sale
    val loading = detailedSaleState.isLoading

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sale Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!editMode) {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Sale")
                        }
                    } else {
                        IconButton(onClick = { viewModel.saveSale() }) {
                            Icon(Icons.Filled.Save, contentDescription = "Save Sale")
                        }
                    }
                    if (!editMode && sale != null) {
                        IconButton(onClick = { showDeleteConfirmation = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Sale")
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
            contentAlignment = Alignment.TopStart
        ) {
            when {
                loading -> CircularProgressIndicator()
                detailedSaleState.error != null -> Text(
                    "Error: ${detailedSaleState.error}",
                    color = MaterialTheme.colorScheme.error
                )
                sale == null -> Text("Sale not found")
                else -> {
                    if (editMode) {
                        var saleDateText by remember { mutableStateOf(sale.saleDate.toString()) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 60.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Edit Sale", style = MaterialTheme.typography.headlineSmall)

                            OutlinedTextField(
                                value = saleDateText,
                                onValueChange = {
                                    saleDateText = it
                                    // TODO: Update ViewModel state for sale date
                                },
                                label = { Text("Sale Date") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Text("Items", style = MaterialTheme.typography.bodyLarge)
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(sale.items) { item ->
                                    SaleItemEditRow(
                                        item = item,
                                        onQuantityChange = { newQuantity ->
                                            viewModel.updateSaleItemQuantity(item.id, newQuantity)
                                        },
                                        onRemoveItem = { itemId ->
                                            viewModel.removeSaleItem(itemId)
                                        }
                                    )
                                }
                            }

                            Text(
                                "Total: ${sale.totalAmount}",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            when (savingState) {
                                is SaleDetailViewModel.SavingState.Saving -> CircularProgressIndicator()
                                is SaleDetailViewModel.SavingState.Error -> Text(
                                    "Save Error: ${(savingState as SaleDetailViewModel.SavingState.Error).message}",
                                    color = MaterialTheme.colorScheme.error
                                )
                                SaleDetailViewModel.SavingState.Success -> {
                                    // Optionally show a success message
                                }
                                else -> {}
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Sale Details", style = MaterialTheme.typography.headlineSmall)
                            Text("Sale ID: ${sale.id}")
                            Text("Client ID: ${sale.clientId}")
                            Text("Sale Date: ${sale.saleDate}")

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Items", style = MaterialTheme.typography.bodyLarge)
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(sale.items) { item ->
                                    SaleItemViewRow(item = item)
                                }
                            }

                            Text(
                                "Total: ${sale.totalAmount}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this sale?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteSale()
                    showDeleteConfirmation = false
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
fun SaleItemViewRow(item: SaleItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text("Product ID: ${item.productId}")
        Text("Quantity: ${item.quantity}")
        Text("Price at Sale: ${item.priceAtSale}")
        Text("Subtotal: ${item.quantity * item.priceAtSale}")
    }
}

@Composable
fun SaleItemEditRow(
    item: SaleItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Product ID: ${item.productId}")
        }
        OutlinedTextField(
            value = item.quantity.toString(),
            onValueChange = { newValue ->
                val quantity = newValue.toIntOrNull() ?: 0
                onQuantityChange(quantity)
            },
            label = { Text("Qty") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(80.dp)
        )
        IconButton(onClick = { onRemoveItem(item.id) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Item"
            )
        }
    }
}
