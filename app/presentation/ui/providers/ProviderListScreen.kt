package com.example.gestiontienda2.presentation.ui.providers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gestiontienda2.domain.models.Provider
import com.example.gestiontienda2.presentation.ui.common.UiState // Assuming UiState is in this path
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme

@Composable
fun ProviderListScreenRoute(
    viewModel: ProviderListViewModel = hiltViewModel(),
    onProviderClick: (Provider) -> Unit,
    onAddProviderClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState() // Assuming ViewModel exposes UiState<List<Provider>>

    ProviderListScreenContent(
        uiState = uiState,
        onProviderClick = onProviderClick,
        onAddProviderClick = onAddProviderClick,
        onRetry = { viewModel.loadProviders() } // Assuming ViewModel has loadProviders or retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderListScreenContent(
    uiState: UiState<List<Provider>>,
    onProviderClick: (Provider) -> Unit,
    onAddProviderClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Providers") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProviderClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Provider")
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
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    val providers = uiState.data
                    if (providers.isEmpty()) {
                        Text("No providers found. Click the + button to add one.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(providers) { provider ->
                                ProviderListItem(provider = provider, onProviderClick = { onProviderClick(provider) })
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

// Previews
@Preview(showBackground = true, name = "Provider List - Success")
@Composable
fun ProviderListScreenSuccessPreview() {
    GestionTiendaAppTheme {
        ProviderListScreenContent(
            uiState = UiState.Success(
                listOf(
                    Provider(id = 1, name = "Supplier Alpha", contactName = "John Doe", phone = "555-1234", email = "john@alpha.com"),
                    Provider(id = 2, name = "Beta Goods Inc.", contactName = "Jane Smith", phone = "555-5678", email = "jane@beta.com")
                )
            ),
            onProviderClick = {},
            onAddProviderClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Provider List - Loading")
@Composable
fun ProviderListScreenLoadingPreview() {
    GestionTiendaAppTheme {
        ProviderListScreenContent(
            uiState = UiState.Loading,
            onProviderClick = {},
            onAddProviderClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Provider List - Error")
@Composable
fun ProviderListScreenErrorPreview() {
    GestionTiendaAppTheme {
        ProviderListScreenContent(
            uiState = UiState.Error("Failed to load providers. Please try again."),
            onProviderClick = {},
            onAddProviderClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Provider List - Empty")
@Composable
fun ProviderListScreenEmptyPreview() {
    GestionTiendaAppTheme {
        ProviderListScreenContent(
            uiState = UiState.Success(emptyList()),
            onProviderClick = {},
            onAddProviderClick = {},
            onRetry = {}
        )
    }
}
