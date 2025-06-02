package com.your_app_name.presentation.ui.financialreports // Replace with your actual package name

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.model.ServiceExpense
import com.your_app_name.presentation.viewmodels.ServiceExpenseReportViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceExpenseReportScreen(
    viewModel: ServiceExpenseReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val serviceExpenses by viewModel.serviceExpensesList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Expense Report") },
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
            // TODO: Add date range display and filtering UI here

            if (serviceExpenses.isEmpty()) {
                Text("No service expenses found for the selected date range.")
            } else {
                // TODO: Display list of service expenses
                // Example:
                LazyColumn {
                    items(serviceExpenses) { expense ->
                        ServiceExpenseItem(expense = expense)
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceExpenseItem(expense: ServiceExpense) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            "Date: ${
                SimpleDateFormat(
                    "yyyy-MM-dd HH:mm",
                    Locale.getDefault()
                ).format(Date(expense.date))
            }"
        )
        Text("Description: ${expense.description}")
        Text("Amount: ${String.format("%.2f", expense.amount)}")
        Divider() // Optional: Add a divider between items
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewServiceExpenseReportScreen() {
    // Provide a mock ViewModel or data for the preview
    ServiceExpenseReportScreen(
        viewModel = object : ServiceExpenseReportViewModel(
            // Mock dependencies if needed
        ) {
            override val serviceExpensesList: StateFlow<List<ServiceExpense>> = MutableStateFlow(
                listOf(
                    ServiceExpense(1, "Rent", 1200.0, System.currentTimeMillis()),
                    ServiceExpense(2, "Utilities", 150.50, System.currentTimeMillis()),
                    ServiceExpense(3, "Internet", 60.0, System.currentTimeMillis())
                )
            ).asStateFlow()
        }
}
}

@Preview(showBackground = true)
@Composable
fun PreviewServiceExpenseReportScreen() {
    ServiceExpenseReportScreen()
}
package com.your_app_name.presentation.ui.financialreports // Replace with your actual package name

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.your_app_name.domain.model.ServiceExpense // Replace with your actual ServiceExpense model
import com.your_app_name.presentation.viewmodels.ServiceExpenseReportViewModel // Replace with your actual ViewModel package

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceExpenseReportScreen(
    viewModel: ServiceExpenseReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val serviceExpenses by viewModel.serviceExpensesList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Expense Report") },
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
            // TODO: Add date range display and filtering UI here

            if (serviceExpenses.isEmpty()) {
                Text("No service expenses found for the selected date range.")
            } else {
                // TODO: Display list of service expenses
                // Example:
                // LazyColumn {
                //     items(serviceExpenses) { expense ->
                //         Text("Date: ${expense.date}, Amount: ${expense.amount}")
                //     }
                // }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewServiceExpenseReportScreen() {
    ServiceExpenseReportScreen()
}