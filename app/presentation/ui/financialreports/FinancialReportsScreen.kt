import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.presentation.viewmodels.FinancialReportsViewModel
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportsScreen(
    navController: NavController
) {
    Scaffold(
    // Collect the financial data from the ViewModel
    val totalSales by viewModel.totalSales.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val netProfit by viewModel.netProfit.collectAsState()

    // Use NumberFormat for currency formatting
    val currencyFormat = remember { NumberFormat.getCurrencyInstance() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Reports") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
            var showStartDatePickerDialog by remember { mutableStateOf(false) }
            var showEndDatePickerDialog by remember { mutableStateOf(false) }

            // Date Selectors
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Start Date:")
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showStartDatePickerDialog = true }) {
                    Text(viewModel.startDate.value.toString()) // Display selected start date
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("End Date:")
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { showEndDatePickerDialog = true }) {
                    Text(viewModel.endDate.value.toString()) // Display selected end date
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Placeholder for DatePickerDialogs (You will need to implement or use a library for this)
            if (showStartDatePickerDialog) {
                // Implement or call your DatePickerDialog here
                // Example: show a simple AlertDialog for now
                AlertDialog(
                    onDismissRequest = { showStartDatePickerDialog = false },
                    title = { Text("Select Start Date") },
                    text = { Text("Date picker goes here.") },
                    confirmButton = {
                        Button(onClick = {
                            // TODO: Handle date selection and update viewModel.startDate.value
                            showStartDatePickerDialog = false
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showEndDatePickerDialog) {
                // Implement or call your DatePickerDialog here
                // Example: show a simple AlertDialog for now
                AlertDialog(
                    onDismissRequest = { showEndDatePickerDialog = false },
                    title = { Text("Select End Date") },
                    text = { Text("Date picker goes here.") },
                    confirmButton = {
                        Button(onClick = {
                            // TODO: Handle date selection and update viewModel.endDate.value
                            showEndDatePickerDialog = false
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Total Sales: ${currencyFormat.format(totalSales)}")
            Text(text = "Total Expenses: ${currencyFormat.format(totalExpenses)}")
            Text(text = "Net Profit: ${currencyFormat.format(netProfit)}")
        }
    }
}