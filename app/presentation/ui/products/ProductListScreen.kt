package com.example.miniproject.presentation.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.miniproject.domain.models.Product


@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit,
    onAddProductClick: () -> Unit
) {
    val productsState by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productsState) { product ->
                    ProductListItem(
                        product = product,
                        onProductClick = onProductClick
                    )
                }
            }

            FloatingActionButton(
                onClick = onAddProductClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Product")
            }
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onProductClick: (Product) -> Unit // Callback for item click
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp) // Reduced vertical padding
            .clickable { onProductClick(product) } // Make the card tappable
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp) // Reduced padding
                .fillMaxWidth()
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.h6,
                maxLines = 1, // Limit name to one line
                overflow = TextOverflow.Ellipsis // Add ellipsis if name is too long
            )
            Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
            Text(text = "Available: ${product.availableStock}", style = MaterialTheme.typography.body2) // Display available stock
            // You might add price or other key info here as well, but keep it concise
        }
    }
}