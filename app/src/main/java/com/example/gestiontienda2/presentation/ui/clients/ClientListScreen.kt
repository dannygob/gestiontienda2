package com.gestiontienda2.presentation.ui.clients

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestiontienda2.domain.models.Client

@Composable
fun ClientListScreen(
    viewModel: ClientListViewModel = hiltViewModel(),
    onClientClick: (Client) -> Unit,
    onAddClientClick: () -> Unit
) {
    val clientsState by viewModel.clients.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClientClick) {
                Icon(Icons.Filled.Add, "Add new client")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(clientsState) { client ->
                        ClientListItem(
                            client = client,
                            onClientClick = onClientClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClientListItem(
    client: Client,
    onClientClick: (Client) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClientClick(client) }
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = client.name,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Add other relevant client details you want to show in the list item, e.g.:
            // Text(text = "Phone: ${client.phone}", style = MaterialTheme.typography.body2)
            // Text(text = "Email: ${client.email}", style = MaterialTheme.typography.body2)
        }
    }
}