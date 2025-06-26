package com.example.gestiontienda2.presentation.ui.clients

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
import com.example.gestiontienda2.domain.models.Client
import com.example.gestiontienda2.presentation.ui.common.UiState // Make sure this path is correct
import com.example.gestiontienda2.presentation.ui.theme.GestionTiendaAppTheme
import java.util.Date // For mock data in previews

@Composable
fun ClientListScreenRoute(
    viewModel: ClientListViewModel = hiltViewModel(),
    onClientClick: (Client) -> Unit,
    onAddClientClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ClientListScreenContent(
        uiState = uiState,
        onClientClick = onClientClick,
        onAddClientClick = onAddClientClick,
        onRetry = { viewModel.retry() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientListScreenContent(
    uiState: UiState<List<Client>>,
    onClientClick: (Client) -> Unit,
    onAddClientClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Clients") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClientClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Client")
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
                    val clients = uiState.data
                    if (clients.isEmpty()) {
                        Text("No clients found. Click the + button to add one.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(clients) { client ->
                                ClientListItem(client = client, onClientClick = { onClientClick(client) })
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

// Preview functions remain the same as I drafted them before.

@Preview(showBackground = true, name = "Client List - Success")
@Composable
fun ClientListScreenSuccessPreview() {
    GestionTiendaAppTheme {
        ClientListScreenContent(
            uiState = UiState.Success(
                listOf(
                    Client(1, "Alice Wonderland", "alice@example.com", "123 Fairy Lane", "555-0101", Date(), null),
                    Client(2, "Bob The Builder", "bob@example.com", "456 Construct Rd", "555-0102", Date(), null)
                )
            ),
            onClientClick = {},
            onAddClientClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Client List - Loading")
@Composable
fun ClientListScreenLoadingPreview() {
    GestionTiendaAppTheme {
        ClientListScreenContent(
            uiState = UiState.Loading,
            onClientClick = {},
            onAddClientClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Client List - Error")
@Composable
fun ClientListScreenErrorPreview() {
    GestionTiendaAppTheme {
        ClientListScreenContent(
            uiState = UiState.Error("Failed to load clients. Please try again."),
            onClientClick = {},
            onAddClientClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, name = "Client List - Empty")
@Composable
fun ClientListScreenEmptyPreview() {
    GestionTiendaAppTheme {
        ClientListScreenContent(
            uiState = UiState.Success(emptyList()),
            onClientClick = {},
            onAddClientClick = {},
            onRetry = {}
        )
    }
}
