import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.models.OrderStatus
import com.your_app_name.presentation.ui.components.DatePickerDialog
import com.your_app_name.presentation.viewmodels.OrderDetailViewModel.SavingState
import java.text.SimpleDateFormat
import java.util.*

ppackage com.your_app_name.presentation.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Delete // Import Delete icon
import androidx.compose.material.icons.filled.ArrowBack // Import ArrowBack for the back icon
import androidx.compose.material.icons.filled.CalendarToday // Import CalendarToday
import androidx.compose.material.icons.filled.Edit // Import Edit icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.models.Order
import com.your_app_name.presentation.viewmodels.OrderDetailViewModel.DetailedOrder
import com.your_app_name.domain.models.OrderStatus // Import OrderStatus
import com.your_app_name.presentation.ui.components.DatePickerDialog // Import DatePickerDialog
import java.text.SimpleDateFormat // Import SimpleDateFormat
import java.util.* // Import Date and Locale
import com.your_app_name.presentation.viewmodels.OrderDetailViewModel.SavingState // Import SavingState

@Composable
fun OrderDetailScreen(
    viewModel: OrderDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val detailedOrderState by viewModel.detailedOrder.collectAsState()
    val orderState by viewModel.order.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState() // Collect savingState

    // State for delete confirmation dialog visibility
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    // State for selected date timestamp (Long) in edit mode
    var editedOrderDateTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    // State for displaying save/error messages
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // LaunchedEffect to show snackbar messages
    LaunchedEffect(savingState) {
        when (savingState) {
            is SavingState.Success -> {
                snackbarMessage = "Operation successful!"
            }

            is SavingState.Error -> {
                snackbarMessage = "Error: ${(savingState as SavingState.Error).message}"
            }

            else -> {
                snackbarMessage = null
            }
        }
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
                            enabled = savingState != SavingState.Saving && // Disable when saving
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
                                enabled = savingState != SavingState.Saving // Disable when saving
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
                    val detailedOrder =
                        detailedOrderState // Get detailed order for client/product names
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Display Order Status
                        Text("Status: ${order.status}", style = MaterialTheme.typography.h6)

                        // Display Client Name
                        Text("Client: ${detailedOrder?.clientName ?: "Loading..."}")

                        // Order ID is typically not editable
                        Text("Order ID: ${order.id}")

                        Spacer(modifier = Modifier.height(16.dp))

                        if (editMode) {
                            // Date Input with DatePicker
                            OutlinedTextField(
                                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                    Date(editedOrderDateTimestamp)
                                ),
                                onValueChange = { /* Read-only, date is selected via picker */ },
                                label = { Text("Order Date") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            Icons.Filled.CalendarToday,
                                            contentDescription = "Select Date"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDatePicker = true
                                    } // Make the entire field clickable
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Items:", style = MaterialTheme.typography.h6)
                            // Display list of order items with product names
                            detailedOrder?.items?.forEach { detailedItem ->
                                Text("- ${detailedItem.productName ?: "Loading..."}: Quantity: ${detailedItem.orderItem.quantity}, Price: ${detailedItem.orderItem.priceAtOrder}")
                            }
                        } else {
                            // Display static order details when not in edit mode
                            Text(
                                "Order Date: ${
                                    SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    ).format(Date(order.orderDate))
                                }"
                            )
                            Text("Total Amount: ${order.totalAmount}")
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Items:", style = MaterialTheme.typography.h6)
                            detailedOrder?.items?.forEach { detailedItem ->
                                Text("- ${detailedItem.productName ?: "Loading..."}: Quantity: ${detailedItem.orderItem.quantity}, Price: ${detailedItem.orderItem.priceAtOrder}")
                            }
                        }


                        // Fulfill Order Button
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.fulfillOrder() }, // Corrected call
                            enabled = order.status != OrderStatus.FULFILLED.name && // Button is enabled if status is not FULFILLED
                                    order.status != OrderStatus.CANCELLED.name && // Also disable if cancelled
                                    savingState != SavingState.Saving, // Disable when saving
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Mark as Fulfilled")
                        }

                        // Cancel Order Button
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.cancelOrder() },
                            enabled = order.status != OrderStatus.CANCELLED.name && // Button is enabled if status is not CANCELLED
                                    order.status != OrderStatus.FULFILLED.name && // Also disable if fulfilled
                                    savingState != SavingState.Saving, // Disable when saving
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Cancel Order")
                        }

                    }
                }

                else -> {
                    Text("Order not found", modifier = Modifier.align(Alignment.Center))
                }
            }

            // Display Saving or Error messages
            when (savingState) {
                is SavingState.Saving -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is SavingState.Success -> {
                    // Display success message (e.g., using a Snackbar)
                    // Text("Saved successfully!", color = Color.Green, modifier = Modifier.align(Alignment.BottomCenter))
                }

                is SavingState.Error -> {
                    // Display error message (e.g., using a Snackbar)
                    // Text("Error: ${(savingState as SavingState.Error).message}", color = Color.Red, modifier = Modifier.align(Alignment.BottomCenter))
                }

                else -> {
                    // No message to display
                }
            }

            // Simple Text display for Snackbar messages (Alternatively use a real Snackbar)
            snackbarMessage?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
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
