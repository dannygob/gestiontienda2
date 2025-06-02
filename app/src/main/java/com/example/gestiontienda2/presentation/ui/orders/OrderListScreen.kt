package app.presentation.ui.orders

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.domain.models.Order

@Composable
fun OrderListScreen(
    viewModel: OrderListViewModel = hiltViewModel(),
    onOrderClick: (Order) -> Unit,
    onAddOrderClick: () -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddOrderClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Order")
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
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage != null -> {
                    Text("Error: $errorMessage")
                }

                orders.isEmpty() -> {
                    Text("No orders found.")
                }

                else -> {
                    LazyColumn {
                        items(orders) { order ->
                            OrderListItem(
                                order = order,
                                onOrderClick = onOrderClick
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}