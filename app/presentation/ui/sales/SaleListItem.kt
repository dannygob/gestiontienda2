package com.your_app_name.presentation.ui.sales

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.your_app_name.domain.models.Sale
import com.your_app_name.domain.models.Client // Assuming you need client name

@Composable
fun SaleListItem(
    sale: Sale,
    onSaleClick: (Sale) -> Unit,
    client: Client? // Assuming you can pass the client for display
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onSaleClick(sale) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Sale ID: ${sale.id}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Date: ${java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(sale.saleDate))}", // Format date
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Client: ${client?.name ?: "Unknown Client"}", // Display client name
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total: $${String.format("%.2f", sale.totalAmount)}", // Format total amount
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}