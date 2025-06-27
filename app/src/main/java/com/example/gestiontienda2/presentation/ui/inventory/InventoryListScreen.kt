package com.example.gestiontienda2.presentation.ui.inventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Product
import com.example.gestiontienda2.presentation.viewmodels.InventoryViewModel // Correct ViewModel import
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme

@Composable
fun InventoryListScreenRoute(
    viewModel: InventoryViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
) {
    val products by viewModel.products.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // LaunchedEffect to load products once when the screen is first composed or when error is null and products are empty
    LaunchedEffect(key1 = true) { // Use a constant key if you only want it to run once
        viewModel.loadProducts()
    }

    InventoryListScreenContent(
        products = if (searchQuery.isBlank()) products else filteredProducts,
        searchQuery = searchQuery,
        loading = loading,
        error = error,
        onProductClick = onProductClick,
        onAddProductClick = onAddProductClick,
        onQueryChange = viewModel::updateSearchQuery,
        onRetry = { viewModel.loadProducts() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreenContent(
    products: List<Product>,
    searchQuery: String,
    loading: Boolean,
    error: String?,
    onProductClick: (Int) -> Unit,
    onAddProductClick: () -> Unit,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory") },
                actions = {
                    IconButton(onClick = onAddProductClick) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Product")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = onQueryChange,
                modifier = Modifier.padding(16.dp)
            )

            when {
                loading && products.isEmpty() -> { // Show loading only if products are empty initially
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    ErrorMessage(
                        error = error,
                        onRetry = onRetry,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    )
                }
                products.isEmpty() -> { // Handles both initial empty and no search results
                     EmptyState(
                        title = if (searchQuery.isBlank()) "No Products" else "No Results",
                        subtitle = if (searchQuery.isBlank()) "Add your first product to get started"
                                   else "No products match your search: \"$searchQuery\"",
                        onActionClick = if (searchQuery.isBlank()) onAddProductClick else null,
                        actionText = if (searchQuery.isBlank()) "Add Product" else null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    ProductList(
                        products = products,
                        onProductClick = onProductClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search products...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        singleLine = true
    )
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductListItem(product = product, onClick = { onProductClick(product.id) })
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                StockStatusChip(stockQuantity = product.stockQuantity)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Stock: ${product.stockQuantity}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    product.category?.let { category ->
                        Text("Category: $category", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("$${String.format("%.2f", product.sellingPrice)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                    Text("Cost: $${String.format("%.2f", product.buyingPrice)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun StockStatusChip(stockQuantity: Int) {
    val (text, containerColor) = when {
        stockQuantity <= 0 -> "Out of Stock" to MaterialTheme.colorScheme.errorContainer
        stockQuantity <= 10 -> "Low Stock" to MaterialTheme.colorScheme.tertiaryContainer // Assuming lowStockThreshold is 10
        else -> "In Stock" to MaterialTheme.colorScheme.primaryContainer
    }
    Surface(color = containerColor, shape = MaterialTheme.shapes.small, modifier = Modifier.padding(4.dp)) {
        Text(text = text, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    onActionClick: (() -> Unit)? = null,
    actionText: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 24.dp))
        if (onActionClick != null && actionText != null) {
            Button(onClick = onActionClick) { Text(actionText) }
        }
    }
}

@Composable
fun ErrorMessage(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Error loading inventory", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        Text(error, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 24.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

// --- Previews ---

@Preview(showBackground = true, name = "Inventory - Populated List")
@Composable
fun InventoryListScreenPopulatedPreview() {
    GestionTiendaAppTheme {
        InventoryListScreenContent(
            products = listOf(
                Product(1, "Laptop Pro", "High-end laptop", 1200.0, 1500.0, 15, "Electronics", "111", null, 1, 5, 0),
                Product(2, "Wireless Mouse", "Ergonomic mouse", 20.0, 35.0, 50, "Accessories", "222", null, 2, 10, 0),
                Product(3, "Keyboard", "Mechanical keyboard", 75.0, 120.0, 5, "Accessories", "333", null, 3, 5, 0)
            ),
            searchQuery = "",
            loading = false,
            error = null,
            onProductClick = {},
            onAddProductClick = {},
            onQueryChange = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Inventory - Empty State")
@Composable
fun InventoryListScreenEmptyPreview() {
    GestionTiendaAppTheme {
        InventoryListScreenContent(
            products = emptyList(),
            searchQuery = "",
            loading = false,
            error = null,
            onProductClick = {},
            onAddProductClick = {},
            onQueryChange = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Inventory - Loading State")
@Composable
fun InventoryListScreenLoadingPreview() {
    GestionTiendaAppTheme {
        InventoryListScreenContent(
            products = emptyList(), // Typically empty when initially loading
            searchQuery = "",
            loading = true,
            error = null,
            onProductClick = {},
            onAddProductClick = {},
            onQueryChange = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Inventory - Error State")
@Composable
fun InventoryListScreenErrorPreview() {
    GestionTiendaAppTheme {
        InventoryListScreenContent(
            products = emptyList(),
            searchQuery = "",
            loading = false,
            error = "Network request failed. Please check connection.",
            onProductClick = {},
            onAddProductClick = {},
            onQueryChange = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductListItem() {
    GestionTiendaAppTheme {
        ProductListItem(
            product = Product(1, "Sample Product", "A sample product for testing", 10.0, 15.0, 25, "Electronics", "123456789", null, 1, 5, 0),
            onClick = {}
        )
    }
}