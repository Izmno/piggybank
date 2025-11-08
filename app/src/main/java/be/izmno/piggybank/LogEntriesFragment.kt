package be.izmno.piggybank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun LogEntriesScreen() {
    val context = LocalContext.current
    
    // Initialize repository
    LaunchedEffect(Unit) {
        LogEntryRepository.initialize(context)
    }
    
    var entries by remember { mutableStateOf<List<LogEntry>>(emptyList()) }
    
    // Update entries list
    fun updateEntriesList() {
        entries = LogEntryRepository.getAllEntries()
    }
    
    // Initial load and update on resume
    LaunchedEffect(Unit) {
        updateEntriesList()
    }
    
    // Update when returning to this screen
    DisposableEffect(Unit) {
        updateEntriesList()
        onDispose { }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (entries.isEmpty()) {
            Text(
                text = "No entries yet",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(top = 32.dp)
            )
        } else {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val amountFormatter = DecimalFormat("#0.00")
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Timestamp
                            Text(
                                text = dateFormat.format(entry.timestamp),
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            
                            // Amount
                            Text(
                                text = "${amountFormatter.format(entry.amount)} EUR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            
                            // Delete button
                            TextButton(
                                onClick = {
                                    LogEntryRepository.deleteEntry(entry)
                                    updateEntriesList()
                                },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "X",
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
