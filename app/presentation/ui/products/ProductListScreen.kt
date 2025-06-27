package com.example.gestiontienda2.presentation.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Correct import for items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.* // M3 components
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme
// Ensure ProductListViewModel is imported if ProductListScreenRoute uses it by default
// import com.example.gestiontienda2.presentation.ui.products.ProductListViewModel

@Composable
fun ProductListScreenRoute(
    viewModel: ProductListViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit,
    onAddProductClick: () -> Unit
) {
    val productsState by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // It's good practice for ViewModel to load initial data if not already loaded
    // If loadProducts is meant to be called on every composition, it's fine.
    // If only once, LaunchedEffect(true) { viewModel.loadProducts() } is better.
    // For now, assuming ViewModel handles its initial load in init or a one-time call.

    ProductListScreenContent(
        products = productsState,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onProductClick = onProductClick,
        onAddProductClick = onAddProductClick,
        onRetry = { viewModel.loadProducts() } // Assuming ViewModel has loadProducts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreenContent(
    products: List<Product>,
    isLoading: Boolean,
    errorMessage: String?,
    onProductClick: (Product) -> Unit,
    onAddProductClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Product")
            }
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
                isLoading && products.isEmpty() -> { // Show loading only if list is empty
                    CircularProgressIndicator()
                }
                errorMessage != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
                products.isEmpty() -> {
                    Text("No products found. Click the + button to add one.")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { product ->
                            ProductListItem(
                                product = product,
                                onProductClick = { onProductClick(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListItem(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        onClick = onProductClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Available: ${product.stockQuantity - product.reservedStockQuantity}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Price: $${String.format("%.2f", product.sellingPrice)}",
                style = MaterialTheme.typography.bodyMedium,
                 color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// --- Previews ---
@Preview(showBackground = true, name = "Product List - Populated")
@Composable
fun ProductListScreenPopulatedPreview() {
    GestionTiendaAppTheme {
        ProductListScreenContent(
            products = listOf(
                Product(1, "Laptop Pro X", "High-end laptop", 1200.0, 1500.0, 15, "Electronics", "111", null, 1, 5, 0, 15),
                Product(2, "Wireless Mouse G", "Ergonomic mouse", 20.0, 35.0, 50, "Accessories", "222", null, 2, 10, 0, 50)
            ),
            isLoading = false,
            errorMessage = null,
            onProductClick = {},
            onAddProductClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Product List - Empty")
@Composable
fun ProductListScreenEmptyPreview() {
    GestionTiendaAppTheme {
        ProductListScreenContent(
            products = emptyList(),
            isLoading = false,
            errorMessage = null,
            onProductClick = {},
            onAddProductClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Product List - Loading")
@Composable
fun ProductListScreenLoadingPreview() {
    GestionTiendaAppTheme {
        ProductListScreenContent(
            products = emptyList(),
            isLoading = true,
            errorMessage = null,
            onProductClick = {},
            onAddProductClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Product List - Error")
@Composable
fun ProductListScreenErrorPreview() {
    GestionTiendaAppTheme {
        ProductListScreenContent(
            products = emptyList(),
            isLoading = false,
            errorMessage = "Failed to load products. Please check your connection.",
            onProductClick = {},
            onAddProductClick = {},
            onRetry = {}
        )
    }
}
