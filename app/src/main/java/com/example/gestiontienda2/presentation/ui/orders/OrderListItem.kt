package app.presentation.ui.orders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.domain.models.Order

@Composable
fun OrderListItem(
    order: Order,
    onOrderClick: (Order) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOrderClick(order) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Order Date: ${order.orderDate}", // You might want to format the date
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Assuming you have a way to get the client name from the order or pass it in
            // Text(
            //     text = "Client: ${order.clientName}",
            //     style = MaterialTheme.typography.bodyMedium
            // )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Status: ${order.status}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total: ${String.format("%.2f", order.totalAmount)}", // Format as currency
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}