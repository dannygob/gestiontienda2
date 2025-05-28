package com.your_app_name.presentation.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Delete // Import Delete icon
import androidx.compose.material.icons.filled.ArrowBack // Import ArrowBack for the back icon
import androidx.compose.material.icons.filled.CalendarToday // Import CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.models.Order
import com.your_app_name.domain.models.OrderStatus // Import OrderStatus
import com.your_app_name.presentation.ui.components.DatePickerDialog // Import DatePickerDialog
import java.text.SimpleDateFormat // Import SimpleDateFormat
import java.util.* // Import Date and Locale

@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val orderState by viewModel.order.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val editMode by viewModel.editMode.collectAsState()

    // State for delete confirmation dialog visibility
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    // State for selected date timestamp (Long) in edit mode
    var editedOrderDateTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

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
 Icons.Filled.ArrowBack, contentDescription = "Back")
                    }

                        Icon(
                },
                actions = {
                    if (orderState != null) {
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(
                                imageVector = if (editMode) Icons.Default.Save else Icons.Default.Edit, // Ensure Icons.Default.Edit is imported if used
                                contentDescription = if (editMode) "Save Order" else "Edit Order"
                            )
                        }
                        if (!editMode) {
                            // Optional: Delete button
                            IconButton(onClick = { showDeleteConfirmation = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Order")
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
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                loading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text("Error: ${error}")
                }
                orderState != null -> {
                    val order = orderState!!
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Order ID is typically not editable
                        Text("Order ID: ${order.id}")

                        if (editMode) {
                            // Date Input with DatePicker
                            OutlinedTextField(
                                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(editedOrderDateTimestamp)),
                                onValueChange = { /* Read-only, date is selected via picker */ },
                                label = { Text("Order Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Filled.CalendarToday, contentDescription = "Select Date")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                            Text("Total Amount: ${order.totalAmount}")

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true } // Make the entire field clickable
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Items:", style = MaterialTheme.typography.h6)
                            // TODO: Display list of order items
                            order.items.forEach { item ->
                                Text("- Product ID: ${item.productId}, Quantity: ${item.quantity}, Price: ${item.priceAtOrder}") // TODO: Display product name
                            }
                        }

                        // Fulfill Order Button
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                viewModel.fulfillOrder(order.id)
                            },
                            enabled = order.status != OrderStatus.FULFILLED.name, // Button is enabled if status is not FULFILLED
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Mark as Fulfilled")
                        }
                    }
                }
                else -> {
                    Text("Order not found")
                }

                    }
                }
                else -> {
                    Text("Order not found")
                }
            }
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