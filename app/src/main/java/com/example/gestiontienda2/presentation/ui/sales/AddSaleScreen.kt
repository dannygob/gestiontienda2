package com.example.gestiontienda2.presentation.ui.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.domain.models.Product.SaleItem
import com.example.gestiontienda2.presentation.viewmodels.SavingState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleScreen(
    viewModel: AddSaleViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val saleDate by viewModel.saleDate.collectAsState()
    val selectedClient by viewModel.selectedClient.collectAsState()
    val saleItems by viewModel.saleItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }

    // Handle successful save
    LaunchedEffect(savingState) {
        if (savingState is SavingState.Success) {
            navigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Sale") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Sale Date Input with DatePicker
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(saleDate)),
                onValueChange = { /* Read-only, date is selected via picker */ },
                label = { Text("Sale Date") },
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
            Spacer(modifier = Modifier.height(16.dp))

            // Client Selection
            Button(
                onClick = {
                    // TODO: Navigate to Client Picker Screen
                    // For now, a placeholder:
                    viewModel.selectClient(
                        Client(
                            id = 1,
                            name = "Sample Client",
                            phone = "123-456-7890",
                            email = "sample@example.com",
                            address = "123 Main St",
                            paymentPreference = "Cash"
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedClient?.name ?: "Select Client")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Product Selection
            Button(
                onClick = {
                    // TODO: Navigate to Product Picker Screen
                    // For now, a placeholder:
                    viewModel.addProductToSale(
                        Product(
                            id = 1,
                            name = "Sample Product",
                            barcode = "111",
                            purchasePrice = 10.0,
                            salePrice = 20.0,
                            category = "Category",
                            stock = 100,
                            providerId = null,
                            imageUrl = null,
                            description = "Sample product description",
                            price = 20.0,
                            stockQuantity = 100,
                            reservedStockQuantity = 0,
                            availableStock = 100,
                            buyingPrice = 10.0,
                            sellingPrice = 20.0
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // List of Added Products
            Text("Items:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(saleItems, key = { it.productId }) { saleItem ->
                    SaleItemInputRow(
                        saleItem = saleItem,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateProductQuantity(saleItem.productId, newQuantity)
                        },
                        onRemoveClick = {
                            viewModel.removeProductFromSale(saleItem.productId)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Total Amount
            Text(
                "Total: $${String.format("%.2f", totalAmount)}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = { viewModel.saveSale() },
                enabled = savingState != SavingState.Saving && selectedClient != null && saleItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (savingState == SavingState.Saving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Sale")
                }
            }

            // Saving State Feedback
            when (savingState) {
                is SavingState.Error -> {
                    Text(
                        text = "Error: ${savingState.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                else -> { /* No additional UI needed */
                }
            }
        }
    }

    // DatePickerDialog
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { timestamp ->
                viewModel.setSaleDate(timestamp)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun SaleItemInputRow(
    saleItem: SaleItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                saleItem.product?.name ?: "Unknown Product",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Price: $${String.format("%.2f", saleItem.priceAtSale)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = saleItem.quantity.toString(),
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
            IconButton(onClick = onRemoveClick) {
                Icon(
                    Icons.Filled.RemoveCircle,
                    contentDescription = "Remove Item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}