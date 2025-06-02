package com.your_app_name.presentation.ui.inventory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.your_app_name.presentation.viewmodels.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryListScreen(
    viewModel: InventoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val products by viewModel.products.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventory") },
                // Add navigation icon if needed for a back button
                // navigationIcon = {
                //     IconButton(onClick = { navController.popBackStack() }) {
                //         Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                //     }
                // }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when {
                loading -> {
                    Text("Loading Inventory...") // Or a CircularProgressIndicator
                }

                error != null -> {
                    Text("Error loading inventory: $error")
                }

                else -> {
                    if (products.isEmpty()) {
                        Text("No products in inventory.")
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(products) { product ->
                                ListItem(
                                    headlineContent = { Text(product.name) },
                                    supportingContent = { Text("Stock: ${product.stockQuantity}") },
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("product_detail/${product.id}")
                                        }
                                        .padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// You would need to add Previews for this Composable
// @Preview(showBackground = true)
// @Composable
// fun PreviewInventoryListScreen() {
//     // Create mock ViewModel and product data for preview
// }