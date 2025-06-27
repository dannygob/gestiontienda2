package com.example.gestiontienda2.presentation.ui.orders


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
// import com.example.gestiontienda2.presentation.ui.addproduct.SavingState // Incorrect import and usage
import com.example.gestiontienda2.presentation.ui.components.DatePickerDialog
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme
import com.example.gestiontienda2.domain.models.OrderStatus // Corrected import
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    // val detailedOrderState by viewModel.detailedOrder.collectAsState() // This seems unused, orderState has items
    val orderState by viewModel.order.collectAsState() // This is Order?
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    // val savingState by viewModel.savingState.collectAsState() // This is Order?, not an enum

    // State for delete confirmation dialog visibility
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    // State for selected date timestamp (Long) in edit mode
    // Initialize with order date if available, or current time
    var editedOrderDateTimestamp by remember(orderState) {
        mutableStateOf(orderState?.orderDate ?: System.currentTimeMillis())
    }


    // Update editedOrderDateTimestamp when orderState changes and in edit mode
    LaunchedEffect(orderState, editMode) {
        if (editMode && orderState != null) {
            editedOrderDateTimestamp = orderState!!.orderDate
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (orderState != null) {
                        // Edit/Save Button
                        IconButton(
                            onClick = {
                                if (editMode) {
                                    viewModel.saveOrder()
                                } else {
                                    viewModel.toggleEditMode()
                                }
                            },
                            enabled = !loading && // Disable when loading (previously savingState)
                                    orderState?.status != OrderStatus.FULFILLED.name && // Disable if fulfilled
                                    orderState?.status != OrderStatus.CANCELLED.name // Disable if cancelled
                        ) {
                            Icon(
                                imageVector = if (editMode) Icons.Default.Save else Icons.Default.Edit,
                                contentDescription = if (editMode) "Save Order" else "Edit Order"
                            )
                        }
                        // Delete button (show only in view mode and if not fulfilled/cancelled)
                        if (!editMode && orderState?.status != OrderStatus.FULFILLED.name && orderState?.status != OrderStatus.CANCELLED.name) {
                            IconButton(
                                onClick = { showDeleteConfirmation = true },
                                enabled = !loading // Disable when loading
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Order"
                                )
                            }
                        }
                    }
                }
            )
        },
        snackbarHost = {
            // Consider using a SnackbarHost to show messages more elegantly
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                error != null -> {
                    Text("Error: ${error}", modifier = Modifier.align(Alignment.Center))
                }

                orderState != null -> {
                    val order = orderState!!
                    // Assuming order.client.name and order.items.product.name are available if populated by UseCase
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Status: ${order.status}", style = MaterialTheme.typography.h6)
                        // Text("Client: ${order.client?.name ?: "Loading..."}") // Assuming Order model has a Client object
                        Text("Client ID: ${order.clientId}") // Displaying ID if client object not embedded
                        Text("Order ID: ${order.id}")
                        Spacer(modifier = Modifier.height(16.dp))

                        if (editMode) {
                            OutlinedTextField(
                                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(editedOrderDateTimestamp)
                                ),
                                onValueChange = { /* Read-only */ },
                                label = { Text("Order Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select Date")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Items:", style = MaterialTheme.typography.h6)
                            order.items.forEach { item ->
                                // Text("- ${item.product?.name ?: "Product ID: ${item.productId}"}: Quantity: ${item.quantity}, Price: ${item.priceAtOrder}")
                                Text("Product ID: ${item.productId}, Qty: ${item.quantity}, Price: ${item.priceAtOrder}")
                            }
                        } else {
                            Text("Order Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(order.orderDate))}")
                            Text("Total Amount: ${order.totalAmount}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Items:", style = MaterialTheme.typography.h6)
                            order.items.forEach { item ->
                                // Text("- ${item.product?.name ?: "Product ID: ${item.productId}"}: Quantity: ${item.quantity}, Price: ${item.priceAtOrder}")
                                Text("Product ID: ${item.productId}, Qty: ${item.quantity}, Price: ${item.priceAtOrder}")
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.fulfillOrder() },
                            enabled = !loading && order.status != OrderStatus.FULFILLED.name && order.status != OrderStatus.CANCELLED.name,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Mark as Fulfilled")
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.cancelOrder() },
                            enabled = !loading && order.status != OrderStatus.CANCELLED.name && order.status != OrderStatus.FULFILLED.name,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Cancel Order")
                        }
                    }
                }
                else -> {
                    Text("Order not found or no details available", modifier = Modifier.align(Alignment.Center))
                }
            }
            // Removed snackbarMessage and the when(savingState) block for displaying messages here.
            // Errors are shown via the 'error' state, loading via 'loading' state.
        }

        // DatePickerDialog
        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { timestamp ->
                    editedOrderDateTimestamp = timestamp
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this order?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteOrder() // Call delete function in ViewModel
                    showDeleteConfirmation = false
                }) { Text("Delete") }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmation = false }) { Text("Cancel") }
            })
    }
}

@Preview(showBackground = true, name = "Order Detail Preview (Limited)")
@Composable
fun OrderDetailScreenPreview() {
    GestionTiendaAppTheme {
        // Note: Previews for screens using Hilt ViewModels with SavedStateHandle
        // are complex. This preview will likely not render the ViewModel's state
        // correctly. It's provided as a basic structural preview.
        // A more robust preview would require refactoring the screen to accept
        // state parameters directly or using a more advanced preview setup with fakes.
        OrderDetailScreen(
            // viewModel = fakeViewModel, // Would need a fake ViewModel instance
            navigateBack = {}
        )
        // Example of what might be needed for a more functional preview:
        // val fakeOrder = Order(id=1, clientId=1, orderDate=System.currentTimeMillis(), status="PENDING", totalAmount=100.0, items=emptyList())
        // OrderDetailScreenContent(order = fakeOrder, loading = false, error = null, editMode = false, /* ... other lambdas ... */)
        // (This would require OrderDetailScreen to be split into a stateful part and a stateless "Content" part)
    }
}
