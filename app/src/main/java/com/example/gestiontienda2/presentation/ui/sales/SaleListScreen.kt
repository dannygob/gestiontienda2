package com.example.gestiontienda2.presentation.ui.sales

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Sale

@Composable
fun SaleListScreen(
    viewModel: SaleListViewModel = hiltViewModel(),
    onSaleClick: (Sale) -> Unit,
    onAddSaleClick: () -> Unit
) {
    val salesState by viewModel.salesState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSaleClick) {
                Icon(Icons.Filled.Add, "Add new sale")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                salesState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                salesState.error != null -> {
                    Text(
                        text = "Error: ${salesState.error}",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                salesState.salesWithClients.isNotEmpty() -> { // Use salesWithClients
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(salesState.salesWithClients) { saleWithClient -> // Change lambda parameter
                            SaleListItem(
                                sale = saleWithClient.sale,
                                onSaleClick = onSaleClick,
                                client = saleWithClient.client
                            ) // Pass sale and client
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No sales found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}