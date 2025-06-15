@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gestiontienda2.presentation.ui.addproduct

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val productName by viewModel.productname.collectAsState()
    val productBarcode by viewModel.productBarcode.collectAsState()
    val productPurchasePrice by viewModel.productPurchasePrice.collectAsState()
    val productSalePrice by viewModel.productSalePrice.collectAsState()
    val productCategory by viewModel.productCategory.collectAsState()
    val productStock by viewModel.productStock.collectAsState()
    val productProviderId by viewModel.productProviderId.collectAsState()
    val savingState by viewModel.savingState.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Add Product") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = productName,
                onValueChange = { viewModel.updateProductName(it) },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productBarcode,
                onValueChange = { viewModel.updateProductBarcode(it) },
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productPurchasePrice,
                onValueChange = { viewModel.updateProductPurchasePrice(it) },
                label = { Text("Purchase Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productSalePrice,
                onValueChange = { viewModel.updateProductSalePrice(it) },
                label = { Text("Sale Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productCategory,
                onValueChange = { viewModel.updateProductCategory(it) },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productStock,
                onValueChange = { viewModel.updateProductStock(it) },
                label = { Text("Stock") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = productProviderId,
                onValueChange = { viewModel.updateProductProviderId(it) },
                label = { Text("Provider ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveProduct() },
                enabled = savingState != SavingState.Saving
            ) {
                Text("Save Product")
            }

            when (savingState) {
                SavingState.Saving -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                is SavingState.Error -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error: ${(savingState as SavingState.Error).message}",
                        color = MaterialTheme.colors.error
                    )
                    LaunchedEffect(savingState) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Error: ${(savingState as SavingState.Error).message}",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }

                SavingState.Success -> {
                    LaunchedEffect(savingState) {
                        navigateBack()
                    }
                }

                else -> {}
            }
        }
    }
}