package com.example.gestiontienda2.presentation.ui.financialreports // Replace with your actual package name

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview // Keep commented out or remove if not needed
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.presentation.viewmodels.SaleReportViewModel

@Composable
fun SaleReportScreen(
    viewModel: SaleReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val sales by viewModel.salesList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sales Report") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
            // Date range display can be added here
            val startDate = viewModel.startDate.collectAsState().value
            val endDate = viewModel.endDate.collectAsState().value

            Text("Report Period:")
            Text(
                "From: ${
                    startDate?.let {
                        java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it))
                    } ?: "N/A"
                } To: ${
                    endDate?.let {
                        java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it))
                    } ?: "N/A"
                }")

            Spacer(modifier = Modifier.height(16.dp))

            if (sales.isEmpty()) {
                Text("No sales found for the selected date range.")
            } else {
                LazyColumn {
                    items(sales) { sale ->
                        SaleReportListItem(sale = sale)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun <Sale> SaleReportListItem(sale: Sale) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

    ) {
        // Assuming Sale model has 'id', 'date' (Long), 'totalAmount', and 'items' (List<OrderItem>)
        Text("Sale ID: ${sale.sale.id}")
        val date = sale.sale.date // Assuming date is Long
        Text("Date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(java.util.Date(date))}")
        Text("Total Amount: $${String.format("%.2f", sale.sale.totalAmount)}") // Format as currency
        Text("Items: ${sale.items.size}")

        // You could add an expansion option here to show individual items
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewSaleReportScreen() {
    SaleReportScreen(onBackClick = {})
}

// You will need to create a SaleReportViewModel and potentially
// update your SaleRepository and Dao to fetch sales by date range.