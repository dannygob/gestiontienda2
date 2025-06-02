package com.gestiontienda2.presentation.ui.serviceexpenses

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gestiontienda2.domain.models.ServiceExpense

@Composable
fun ServiceExpenseListScreen(
    viewModel: ServiceExpenseListViewModel = hiltViewModel(),
    onServiceExpenseClick: (ServiceExpense) -> Unit,
    onAddServiceExpenseClick: () -> Unit
) {
    val serviceExpenses by viewModel.serviceExpenses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddServiceExpenseClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Service Expense")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                serviceExpenses.isEmpty() -> {
                    Text(
                        text = "No service expenses found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(serviceExpenses) { serviceExpense ->
                            ServiceExpenseListItem(
                                serviceExpense = serviceExpense,
                                onServiceExpenseClick = onServiceExpenseClick
                            )
                        }
                    }
                }
            }
        }
    }
}