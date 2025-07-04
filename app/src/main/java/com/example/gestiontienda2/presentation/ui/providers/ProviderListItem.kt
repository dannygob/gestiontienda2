package com.example.gestiontienda2.presentation.ui.providers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.gestiontienda2.domain.models.Provider

@Composable
fun ProviderListItem(
    provider: Provider, // Changed from Int to Provider
    onProviderClick: (Provider) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onProviderClick(provider) } // Pass the Provider object
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = provider.name, // Now correctly accesses Provider.name
                style = MaterialTheme.typography.titleMedium, // Using M3 typography
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            provider.phone?.let {
                Text(text = "Phone: $it", style = MaterialTheme.typography.body2)
            }
            // You might add email or other key info here as well, but keep it concise
        }
    }
}