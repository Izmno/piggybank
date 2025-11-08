package be.izmno.piggybank

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    
    // Initialize repository
    LaunchedEffect(Unit) {
        LogEntryRepository.initialize(context)
    }
    
    var totalAmount by remember { mutableStateOf(0.0) }
    var showCustomAmountDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    
    // Update total amount
    fun updateTotalAmount() {
        totalAmount = LogEntryRepository.getTotalAmount()
    }
    
    // Initial load and update on resume
    LaunchedEffect(Unit) {
        updateTotalAmount()
    }
    
    // Update when returning to this screen
    DisposableEffect(Unit) {
        updateTotalAmount()
        onDispose { }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // PiggyBankDisplay
        PiggyBankDisplay(
            text = "€${DecimalFormat("#0.00").format(totalAmount)}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Add €15 Button
        Button(
            onClick = {
                val entry = LogEntry(
                    timestamp = System.currentTimeMillis(),
                    amount = 15.0
                )
                LogEntryRepository.addEntry(entry)
                updateTotalAmount()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.click_button),
                fontSize = 18.sp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Custom Amount Button
        Text(
            text = stringResource(R.string.add_custom_amount),
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color(0xFF1976D2), // holo_blue_dark
            modifier = Modifier.clickable {
                showCustomAmountDialog = true
            }
        )
    }
    
    // Custom Amount Dialog
    if (showCustomAmountDialog) {
        AlertDialog(
            onDismissRequest = { showCustomAmountDialog = false },
            title = { Text(stringResource(R.string.dialog_add_custom_amount_title)) },
            text = {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(stringResource(R.string.dialog_amount_label)) },
                    placeholder = { Text("0.00") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val inputText = amountText.trim()
                        if (inputText.isNotEmpty()) {
                            try {
                                val amount = inputText.toDouble()
                                if (amount > 0) {
                                    val entry = LogEntry(
                                        timestamp = System.currentTimeMillis(),
                                        amount = amount
                                    )
                                    LogEntryRepository.addEntry(entry)
                                    updateTotalAmount()
                                    showCustomAmountDialog = false
                                    amountText = ""
                                } else {
                                    errorMessage = stringResource(R.string.dialog_invalid_amount)
                                    showErrorDialog = true
                                }
                            } catch (e: NumberFormatException) {
                                errorMessage = stringResource(R.string.dialog_invalid_amount)
                                showErrorDialog = true
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCustomAmountDialog = false
                        amountText = ""
                    }
                ) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
    
    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
