package com.example.gestiontienda2.presentation.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gestiontienda2.domain.models.OrderItem
import com.example.gestiontienda2.presentation.ui.components.DatePickerDialog
import com.example.gestiontienda2.presentation.viewmodels.SavingState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
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
            Text("Client:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showClientDialog = true }) {
                Text(selectedClient?.name ?: "Select Client")
            }
            Spacer(Modifier.height(16.dp))

            Text("Order Date:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Date(viewModel.orderDate.collectAsStateWithLifecycle().value)),
                onValueChange = {},
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            Text("Items:", style = MaterialTheme.typography.titleMedium)
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
                        onRemoveItem = {
                            viewModel.removeOrderItem(item.productId)
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            Text(
                "Total: $${"%.2f".format(totalAmount)}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveOrder(viewModel.orderDate.value) },
                enabled = savingState !is SavingState.Saving &&
                        selectedClient != null && orderItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                when (savingState) {
                    is SavingState.Idle -> Text("Save Order")
                    is SavingState.Saving -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    is SavingState.Success -> Text("Saved!")
                    is SavingState.Error -> Text("Error Saving")
                    is AddOrderViewModel.SavingState.Error -> TODO()
                    AddOrderViewModel.SavingState.Idle -> TODO()
                    AddOrderViewModel.SavingState.Loading -> TODO()
                    AddOrderViewModel.SavingState.Success -> TODO()
                }
            }

            LaunchedEffect(savingState) {
                if (savingState is SavingState.Success) {
                    navigateBack()
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = {
                viewModel.updateOrderDate(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

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
                            Text("${product.name} ($${"%.2f".format(product.salePrice)})")
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
                    onQuantityChange(newValue.toIntOrNull() ?: 0)
                },
                label = { Text("Qty") },
                modifier = Modifier.width(80.dp),
                singleLine = true,
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onRemoveItem) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove Item"
                )
            }
        }
    }
}
