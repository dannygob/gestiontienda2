package com.your_app_name.presentation.ui.productdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Product

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val productState by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val savingState by viewModel.savingState.collectAsState()
    val stockAdjustmentQuantity by viewModel.stockAdjustmentQuantity.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (editMode) "Edit Product" else "Product Details") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isLoading && errorMessage == null) {
                        if (editMode) {
                            IconButton(onClick = viewModel::saveProduct) {
                                Icon(Icons.Filled.Save, contentDescription = "Save")
                            }
                        } else {
                            IconButton(onClick = viewModel::toggleEditMode) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit")
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
                isLoading -> CircularProgressIndicator()
                errorMessage != null -> Text(text = errorMessage!!, color = MaterialTheme.colors.error)
                productState != null -> {
                    val product = productState!!
                    if (editMode) {
                        // Edit Mode UI
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = product.name,
                                onValueChange = { viewModel.updateProductName(it) }, // Assuming update function exists
                                label = { Text("Product Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.barcode,
                                onValueChange = { viewModel.updateProductBarcode(it) }, // Assuming update function exists
                                label = { Text("Barcode") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.purchasePrice.toString(),
                                onValueChange = { viewModel.updateProductPurchasePrice(it.toDoubleOrNull() ?: 0.0) }, // Assuming update function exists
                                label = { Text("Purchase Price") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.salePrice.toString(),
                                onValueChange = { viewModel.updateProductSalePrice(it.toDoubleOrNull() ?: 0.0) }, // Assuming update function exists
                                label = { Text("Sale Price") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.category,
                                onValueChange = { viewModel.updateProductCategory(it) }, // Assuming update function exists
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.stockQuantity.toString(), // Assuming product.stockQuantity is the field to edit
                                onValueChange = { viewModel.updateProductStock(it.toIntOrNull() ?: 0) },
                                label = { Text("Stock Quantity") }, // Changed label
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = product.providerId ?: "", // Assuming providerId can be null
                                onValueChange = { viewModel.updateProductProviderId(it) },
                                label = { Text("Provider ID") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        // View Mode UI
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Name: ${product.name}", style = MaterialTheme.typography.h6)
                            Text(text = "Barcode: ${product.barcode ?: "N/A"}")
                            Text(text = "Purchase Price: ${product.purchasePrice}")
                            Text(text = "Sale Price: ${product.salePrice}")
                            Text(text = "Category: ${product.category ?: "N/A"}")
                            Text(text = "Available Stock: ${product.availableStock ?: 0}")
                            Text(text = "Total Stock: ${product.stockQuantity ?: 0}")
                            Text(text = "Reserved Stock: ${product.reservedStockQuantity ?: 0}")
                            Text(text = "Provider ID: ${product.providerId ?: "N/A"}")

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Stock Adjustment", style = MaterialTheme.typography.h6)

                            OutlinedTextField(
                                value = stockAdjustmentQuantity,
                                onValueChange = { viewModel.updateStockAdjustmentQuantity(it) },
                                label = { Text("Quantity") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = viewModel::increaseStock,
                                    enabled = savingState != ProductDetailViewModel.SavingState.AdjustingStock
                                ) {
                                    Text("Increase Stock")
                                }
                                Button(
                                    onClick = viewModel::decreaseStock,
                                    enabled = savingState != ProductDetailViewModel.SavingState.AdjustingStock
                                ) {
                                    Text("Decrease Stock")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp)) // Add spacing before feedback

                            // Feedback for saving and stock adjustment
                            when (savingState) {
                                ProductDetailViewModel.SavingState.Saving -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                                ProductDetailViewModel.SavingState.Success -> Text("Product saved successfully!", color = MaterialTheme.colors.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                                is ProductDetailViewModel.SavingState.Error -> Text("Save error: ${savingState.message}", color = MaterialTheme.colors.error, modifier = Modifier.align(Alignment.CenterHorizontally))

                                ProductDetailViewModel.SavingState.AdjustingStock -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                                ProductDetailViewModel.SavingState.StockAdjustedSuccess -> Text("Stock adjusted successfully!", color = MaterialTheme.colors.primary, modifier = Modifier.align(Alignment.CenterHorizontally))
                                is ProductDetailViewModel.SavingState.StockAdjustmentError -> Text("Stock adjustment error: ${savingState.message}", color = MaterialTheme.colors.error, modifier = Modifier.align(Alignment.CenterHorizontally))

                                else -> {} // Hide feedback when not adjusting or idle
                            }

                            // Basic saving state indication for edit mode
                            if (editMode && savingState == ProductDetailViewModel.SavingState.Saving) CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            if (editMode && savingState is ProductDetailViewModel.SavingState.Error) Text("Save Error: ${(savingState as ProductDetailViewModel.SavingState.Error).message}", color = MaterialTheme.colors.error, modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }
    }
}
