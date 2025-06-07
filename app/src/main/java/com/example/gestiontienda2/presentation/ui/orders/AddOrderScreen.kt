package com.example.gestiontienda2.presentation.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.domain.models.OrderItem
import com.example.gestiontienda2.presentation.ui.components.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddOrderScreen(
    viewModel: AddOrderViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val selectedClient by viewModel.selectedClient.collectAsStateWithLifecycle()
    val orderItems by viewModel.orderItems.collectAsStateWithLifecycle()
    val totalAmount by viewModel.totalAmount.collectAsStateWithLifecycle()
    val savingState by viewModel.savingState.collectAsStateWithLifecycle()
    val clients by viewModel.clients.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    var showClientDialog by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }
    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Order") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Client Selection
            Text("Client:", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showClientDialog = true }) {
                Text(selectedClient?.name ?: "Select Client")
            }
            Spacer(Modifier.height(16.dp))

            // Date Input
            Text("Order Date:", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date(viewModel.orderDate.collectAsStateWithLifecycle().value)),
                onValueChange = { /* Read-only, date is selected via picker */ },
                label = { Text("Date") },
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

            // Product Selection and List
            Text("Items:", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showProductDialog = true }) {
                Text("Add Product")
            }
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(orderItems) { item ->
                    OrderItemInputRow(
                        orderItem = item,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateOrderItemQuantity(item.productId, newQuantity)
                        },
                        onRemoveItem = { viewModel.removeOrderItem(item.productId) }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            // Total Amount
            Text("Total: $${"%.2f".format(totalAmount)}", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveOrder(viewModel.orderDate.value) }, // Pass the selected date timestamp
                enabled = savingState != SavingState.Saving && selectedClient != null && orderItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                when (savingState) {
                    SavingState.Idle -> Text("Save Order")
                    SavingState.Saving -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colors.onPrimary
                    )

                    SavingState.Success -> Text("Saved!")
                    SavingState.Error -> Text("Error Saving")
                }
            }

            // Observe saving state for navigation
            LaunchedEffect(savingState) {
                if (savingState == SavingState.Success) {
                    navigateBack()
                }
            }
        }
    }

    // DatePickerDialog
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { timestamp ->
                viewModel.updateOrderDate(timestamp) // Update the date in the ViewModel
                showDatePicker = false // Dismiss the picker after selection
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Client Selection Dialog
    if (showClientDialog) {
        AlertDialog(
            onDismissRequest = { showClientDialog = false },
            title = { Text("Select Client") },
            text = {
                LazyColumn {
                    items(clients) { client ->
                        TextButton(onClick = {
                            viewModel.selectClient(client)
                            showClientDialog = false
                        }) {
                            Text(client.name)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showClientDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Product Selection Dialog
    if (showProductDialog) {
        AlertDialog(
            onDismissRequest = { showProductDialog = false },
            title = { Text("Select Product") },
            text = {
                LazyColumn {
                    items(products) { product ->
                        TextButton(onClick = {
                            viewModel.addOrderItem(product)
                            showProductDialog = false
                        }) {
                            Text("${product.name} ($${"%.2f".format(product.price)})")
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showProductDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun OrderItemInputRow(
    orderItem: OrderItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(orderItem.product?.name ?: "Unknown Product")
            Text("Price: $${"%.2f".format(orderItem.priceAtOrder)}")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = orderItem.quantity.toString(),
                onValueChange = { newValue ->
                    val quantity = newValue.toIntOrNull() ?: 0
                    onQuantityChange(quantity)
                },
                label = { Text("Qty") },
                modifier = Modifier.width(80.dp),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Number)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onRemoveItem) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Remove Item",
                    modifier = Modifier.mirrorHorizontally()
                ) // Using Add icon mirrored for simplicity
            }
        }
    }
}

// Simple mirroring extension for the Icon
fun Modifier.mirrorHorizontally(): Modifier = this.then(Modifier.scale(scaleX = -1f, scaleY = 1f))

// Simple mirroring extension for the Icon
fun Modifier.mirrorHorizontally(): Modifier = this.then(Modifier.scale(scaleX = -1f, scaleY = 1f))

// Enum for saving state (can be defined in ViewModel or a common utility)
enum class SavingState {
    Idle, Saving, Success, Error
}
