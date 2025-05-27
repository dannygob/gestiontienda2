package com.your_app_name.presentation.ui.purchases

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.models.Product
import com.your_app_name.domain.models.Provider
import com.your_app_name.domain.models.PurchaseItem
import com.your_app_name.presentation.viewmodels.AddPurchaseViewModel // Assuming AddPurchaseViewModel is in viewmodels package
import com.your_app_name.presentation.ui.components.DatePickerDialog // Import the DatePickerDialog
import com.your_app_name.util.SavingState // Assuming SavingState sealed class
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddPurchaseScreen(
    viewModel: AddPurchaseViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val selectedProvider by viewModel.selectedProvider.collectAsState()
    val purchaseItems by viewModel.purchaseItems.collectAsState()
    val totalAmount by viewModel.totalAmount.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    // State for DatePicker visibility
    var showDatePicker by remember { mutableStateOf(false) }
    // State for selected date timestamp (Long)
    var selectedDateTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Purchase") },
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
            // Provider Selection (Basic representation, would likely be a navigation to a picker)
            Button(onClick = {
                // TODO: Navigate to Provider Picker Screen and handle selection result
                // For now, a placeholder:
                viewModel.selectProvider(Provider(id = 1, name = "Sample Provider", phone = "123-456-7890", email = "sample@example.com", address = "123 Provider St"))
            }) {
                Text(selectedProvider?.name ?: "Select Provider")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Product Selection (Basic representation, would likely be a navigation to a picker)
            Button(onClick = {
                // TODO: Navigate to Product Picker Screen and handle selection result
                // For now, a placeholder:
                viewModel.addProductToPurchase(Product(id = 1, name = "Sample Product", barcode = "111", purchasePrice = 10.0, salePrice = 20.0, category = "Category", stock = 100, providerId = null))
            }) {
                Text("Add Product") // Add closing parenthesis
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Date Input
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDateTimestamp)),
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
            })
            Spacer(modifier = Modifier.height(16.dp))

            // List of Added Products
            Text("Items:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(purchaseItems, key = { it.product?.id ?: it.productId }) { purchaseItem ->
                    PurchaseItemInputRow(
                        purchaseItem = purchaseItem,
                        onQuantityChange = { newQuantity ->
                            viewModel.updateProductQuantity(purchaseItem.product?.id ?: purchaseItem.productId, newQuantity)
                        },
                        onRemoveClick = {
                            viewModel.removeProductFromPurchase(purchaseItem.product?.id ?: purchaseItem.productId)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Total Amount
            Text("Total: $${String.format("%.2f", totalAmount)}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    // Pass the selected date to the ViewModel when saving
                    viewModel.savePurchase(selectedDateTimestamp)
                },
                enabled = savingState != SavingState.Saving
            ) {
                if (savingState == SavingState.Saving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Purchase")
                }
            }

            // Saving State Feedback
            when (savingState) {
                is SavingState.Error -> {
                    Text(
                        text = "Error: ${(savingState as SavingState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                is SavingState.Success -> {
                    Text(
                        text = "Purchase saved successfully!",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    // Navigate back on success
                     LaunchedEffect(Unit) { // Use LaunchedEffect to navigate after composition
                        navigateBack()
                    }
                }
                else -> {} // Idle or Saving, no extra text needed below button
            }
        }

        // DatePickerDialog
        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { timestamp ->
                    selectedDateTimestamp = timestamp
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
fun PurchaseItemInputRow(
    purchaseItem: PurchaseItem,
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
            Text(purchaseItem.product?.name ?: "Unknown Product", style = MaterialTheme.typography.bodyMedium)
            Text("Price: $${String.format("%.2f", purchaseItem.priceAtPurchase)}", style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = purchaseItem.quantity.toString(),
                onValueChange = { newValue ->
                    val quantity = newValue.toIntOrNull() ?: 0
                    onQuantityChange(quantity)
                },
                label = { Text("Qty") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(80.dp),
                singleLine = true
            )
            IconButton(onClick = onRemoveClick) {
                Icon(Icons.Filled.RemoveCircle, contentDescription = "Remove Item", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}