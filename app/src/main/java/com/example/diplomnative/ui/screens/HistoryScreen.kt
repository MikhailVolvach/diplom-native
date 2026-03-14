package com.example.diplomnative.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.theme.BankGreen
import com.example.diplomnative.ui.theme.BankOnBackgroundText
import com.example.diplomnative.ui.theme.BankScreenBackground
import com.example.diplomnative.ui.theme.DiplomNativeTheme

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: String,
    val date: String,
    val type: TransactionType,
    val icon: ImageVector
)

enum class TransactionType {
    INCOME, EXPENSE
}

val mockTransactions = listOf(
    Transaction("1", "Супермаркет 'Магнит'", "Продукты", "- 1 240,00 ₽", "Сегодня, 14:20", TransactionType.EXPENSE, Icons.Default.ShoppingCart),
    Transaction("2", "Пополнение счета", "Переводы", "+ 50 000,00 ₽", "Сегодня, 10:05", TransactionType.INCOME, Icons.Default.ArrowBack),
    Transaction("3", "Вкусно и точка", "Кафе и рестораны", "- 650,00 ₽", "Вчера, 19:30", TransactionType.EXPENSE, Icons.Default.ShoppingCart),
    Transaction("4", "Оплата ЖКХ", "Платежи", "- 4 200,00 ₽", "Вчера, 12:00", TransactionType.EXPENSE, Icons.Filled.Home),
    Transaction("5", "Перевод Михаилу", "Переводы", "- 1 000,00 ₽", "25 мая, 16:45", TransactionType.EXPENSE, Icons.Default.ArrowForward),
    Transaction("6", "Зарплата", "Работа", "+ 85 000,00 ₽", "20 мая, 09:00", TransactionType.INCOME, Icons.Default.ArrowBack),
    Transaction("7", "Яндекс Go", "Транспорт", "- 450,00 ₽", "19 мая, 21:15", TransactionType.EXPENSE, Icons.Default.Info)
)

@Composable
fun HistoryScreen(modifier: Modifier = Modifier) {
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(mockTransactions) { transaction ->
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

@Composable
fun TransactionItem(transaction: Transaction) {
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
                    if (transaction.type == TransactionType.INCOME) 
                        BankGreen.copy(alpha = 0.1f) 
                    else 
                        Color.Gray.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transaction.icon,
                contentDescription = null,
                tint = if (transaction.type == TransactionType.INCOME) BankGreen else BankOnBackgroundText,
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
                text = transaction.date,
                style = MaterialTheme.typography.bodySmall,
                color = BankOnBackgroundText.copy(alpha = 0.6f)
            )
        }

        // Сумма
        Text(
            text = transaction.amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transaction.type == TransactionType.INCOME) BankGreen else BankOnBackgroundText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    DiplomNativeTheme {
        HistoryScreen()
    }
}
