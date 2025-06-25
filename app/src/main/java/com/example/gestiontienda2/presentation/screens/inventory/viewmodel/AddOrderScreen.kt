package com.example.gestiontienda2.presentation.screens.inventory.viewmodel

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gestiontienda2.presentation.viewmodels.AddOrderViewModel
import com.your_app_name.presentation.ui.UiEvent
import com.your_app_name.util.SavingState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    viewModel: AddOrderViewModel,
    onBack: () -> Unit
) {
    val order by viewModel.newOrder.collectAsState()
    val savingState by viewModel.savingState.collectAsState()
    val context = LocalContext.current

    // Escuchar eventos del ViewModel
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Pedido") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveOrder() },
                enabled = savingState != SavingState.Saving
            ) {
                Icon(Icons.Default.Save, contentDescription = "Guardar Pedido")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Cliente
            Text("Cliente ID: ${if (order.clientId >= 0) order.clientId else "No seleccionado"}", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { /* TODO: Implementar selección de cliente */ }) {
                Text("Seleccionar Cliente")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha
            val formattedDate = remember(order.orderDate) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(order.orderDate))
            }

            Text("Fecha del Pedido: $formattedDate")
            Button(onClick = {
                viewModel.updateOrderDate(System.currentTimeMillis())
            }) {
                Text("Actualizar Fecha")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Productos
            Text("Productos", style = MaterialTheme.typography.titleMedium)
            if (order.items.isEmpty()) {
                Text("No hay productos agregados.")
            } else {
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Producto ID: ${item.productId}")
                            Text("Cantidad: ${item.quantity}")
                        }
                        IconButton(onClick = { viewModel.removeItem(item.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar Producto")
                        }
                    }
                }
            }

            Button(onClick = { /* TODO: Implementar agregar producto */ }) {
                Text("Agregar Producto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total
            Text("Total: \$${"%.2f".format(order.totalAmount)}", style = MaterialTheme.typography.titleLarge)

            if (savingState == SavingState.Saving) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
