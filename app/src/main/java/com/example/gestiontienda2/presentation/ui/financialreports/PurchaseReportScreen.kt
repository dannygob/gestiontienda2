package com.example.gestiontienda2.presentation.ui.financialreports // Replace with your actual package name

import androidx.camera.core.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.tiles.tooling.preview.Preview
import com.example.gestiontienda2.presentation.viewmodels.PurchaseReportViewModel
import com.gestiontienda2.ui.theme.YourAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseReportScreen(
    viewModel: PurchaseReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    // Observe the list of purchases from the ViewModel
    val purchases by viewModel.purchasesList.collectAsState()
    // You'll need to handle loading and error states here as well

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase Report") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder for date range display (you'll need to get this from the ViewModel)
            Text(
                text = "Report for Date Range: ", // Display actual date range here
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Using LazyColumn to display the list of purchases
            LazyColumn {
                if (purchases.isEmpty()) {
                    item {
                        Text(
                            text = "No purchases found for this date range.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    items(purchases) { purchase ->
                        // Basic display for each purchase item
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("Date: ${purchase.purchase.date}") // Adjust date format as needed
                            Text("Total Amount: ${purchase.purchase.totalAmount}") // Format as currency
                            Text("Items: ${purchase.items.size}")
                            Divider() // Add a separator between items
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPurchaseReportScreen() {
    YourAppTheme { // Replace with your actual theme
        PurchaseReportScreen(onBackClick = {})
    }
}