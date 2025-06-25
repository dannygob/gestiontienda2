package com.your_app_name.presentation.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.DatePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog

@Composable
fun DatePickerDialog(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Select Date")
            },
            text = {
                DatePicker(state = datePickerState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
                        }
                        onDismiss()
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

import androidx.compose.runtime.derivedStateOf