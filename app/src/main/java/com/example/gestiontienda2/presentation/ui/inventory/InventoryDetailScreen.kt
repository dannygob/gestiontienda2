package com.bizsync.bizsync.presentation.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bizsync.bizsync.presentation.viewmodels.InventoryDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryDetailScreen(
    viewModel: InventoryDetailViewModel = hiltViewModel(),
    productId: Int,
    navigateBack: () -> Unit
) {
    val product by viewModel.product.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                loading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text("Error: ${error}")
                }

                product == null -> {
                    Text("Product not found")
                }

                else -> {
                    val currentProduct = product!!
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Details for ${currentProduct.name}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Description: ${currentProduct.description}")
                        Text("Buying Price: ${currentProduct.buyingPrice}")
                        Text("Selling Price: ${currentProduct.sellingPrice}")
                        Text("Current Stock: ${currentProduct.stockQuantity}")
                        // Add more product details here as needed
                    }
                }
            }
        }
    }
}