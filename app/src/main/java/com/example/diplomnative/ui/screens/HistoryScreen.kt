package com.example.diplomnative.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diplomnative.data.TransactionEntity
import com.example.diplomnative.ui.theme.*
import com.example.diplomnative.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.allTransactions.collectAsState()

    Scaffold(
        containerColor = BankScreenBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "История операций",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BankOnBackgroundText,
                modifier = Modifier.padding(24.dp)
            )

            if (transactions.isEmpty()) {
                EmptyHistoryState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            thickness = 0.5.dp,
                            color = BankOnBackgroundText.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistoryState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = BankOnBackgroundText.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "У вас пока нет операций",
                style = MaterialTheme.typography.bodyLarge,
                color = BankOnBackgroundText.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка категории
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    if (transaction.isIncome) 
                        BankGreen.copy(alpha = 0.1f) 
                    else 
                        Color.Gray.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getIconForName(transaction.iconName),
                contentDescription = null,
                tint = if (transaction.isIncome) BankGreen else BankOnBackgroundText,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Название и дата
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = BankOnBackgroundText
            )
            Text(
                text = transaction.date.formatTimestamp(),
                style = MaterialTheme.typography.bodySmall,
                color = BankOnBackgroundText.copy(alpha = 0.6f)
            )
        }

        // Сумма
        Text(
            text = (if (transaction.isIncome) "+ " else "- ") + transaction.amount.formatBalance(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isIncome) BankGreen else BankOnBackgroundText
        )
    }
}

private fun Long.formatTimestamp(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd MMMM, HH:mm", Locale("ru"))
    return formatter.format(date)
}

private fun getIconForName(name: String): ImageVector {
    return when (name) {
        "Send" -> Icons.AutoMirrored.Filled.Send
        "ShoppingCart" -> Icons.Default.ShoppingCart
        "ArrowBack" -> Icons.AutoMirrored.Filled.ArrowBack
        "Home" -> Icons.Default.Home
        "Info" -> Icons.Default.Info
        "Fastfood" -> Icons.Default.ShoppingCart
        "Payments" -> Icons.Default.Info
        else -> Icons.Default.Info
    }
}
