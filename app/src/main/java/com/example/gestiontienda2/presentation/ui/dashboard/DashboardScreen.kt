package com.gestiontienda2.presentation.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToProviders: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onNavigateToProducts,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Products")
            }
            Button(
                onClick = onNavigateToClients,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Clients")
            }
            Button(
                onClick = onNavigateToProviders,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Providers")
            }
        }
    }
}