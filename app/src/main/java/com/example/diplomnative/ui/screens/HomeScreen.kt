package com.example.diplomnative.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.theme.DiplomNativeTheme

// Моковые данные
data class BankCard(val id: String, val name: String, val balance: String, val color: Color)
data class AdBanner(val title: String, val description: String, val color: Color)
data class Tip(val title: String, val description: String)

val mockCards = listOf(
    BankCard("1", "Основная карта", "45 200,00 ₽", Color(0xFF1A237E)),
    BankCard("2", "Сберегательный", "150 000,50 ₽", Color(0xFF2E7D32)),
    BankCard("3", "Кредитка", "10 000,00 ₽", Color(0xFFB71C1C))
)

val mockAds = listOf(
    AdBanner("Кредит наличными", "От 5.5% годовых", Color(0xFFBBDEFB)),
    AdBanner("Ипотека", "Ставка от 4.7%", Color(0xFFC8E6C9)),
    AdBanner("Инвестиции", "Начните с 1000₽", Color(0xFFFFF9C4))
)

val mockTips = listOf(
    Tip("Безопасность", "Никому не сообщайте CVC-код вашей карты."),
    Tip("Кешбэк", "Выбирайте категории месяца, чтобы получать больше бонусов."),
    Tip("Лимиты", "Установите лимиты на покупки для контроля расходов.")
)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            GreetingSection()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { BalanceSection(mockCards) }
            item { AdsSection(mockAds) }
            item { TipsSection(mockTips) }
        }
    }
}

@Composable
fun GreetingSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Привет, Михаил!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Рады видеть вас снова",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = { /* TODO: Уведомления */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "Уведомления")
        }
    }
}

@Composable
fun BalanceSection(cards: List<BankCard>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ваши счета",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cards) { card ->
                CardItem(card)
            }
        }
    }
}

@Composable
fun CardItem(card: BankCard) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = card.color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = card.name,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = card.balance,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AdsSection(ads: List<AdBanner>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Спецпредложения",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ads) { ad ->
                AdItem(ad)
            }
        }
    }
}

@Composable
fun AdItem(ad: AdBanner) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ad.color)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = ad.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = ad.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun TipsSection(tips: List<Tip>) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "Советы дня",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        tips.forEach { tip ->
            TipItem(tip)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TipItem(tip: Tip) {
    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tip.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = tip.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DiplomNativeTheme {
        HomeScreen()
    }
}
