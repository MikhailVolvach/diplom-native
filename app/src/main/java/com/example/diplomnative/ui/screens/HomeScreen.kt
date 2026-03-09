package com.example.diplomnative.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.theme.DiplomNativeTheme
import kotlinx.coroutines.delay
import kotlin.concurrent.timer
import androidx.compose.runtime.setValue // Add this line
import com.example.diplomnative.ui.theme.AdCreditBg
import com.example.diplomnative.ui.theme.AdInvestBg
import com.example.diplomnative.ui.theme.AdMortgageBg
import com.example.diplomnative.ui.theme.BankOnContainerText
import com.example.diplomnative.ui.theme.CardGraphite
import com.example.diplomnative.ui.theme.CardRuby
import com.example.diplomnative.ui.theme.CardSapphire
import com.example.diplomnative.ui.theme.OnAdCredit
import com.example.diplomnative.ui.theme.OnAdInvest
import com.example.diplomnative.ui.theme.OnAdMortgage
import com.example.diplomnative.ui.widgets.GreetingSection

// Моковые данные
data class BankCard(val id: String, val name: String, val balance: String, val color: Color)
data class AdBanner(val title: String, val description: String, val color: Color)
data class Tip(val title: String, val description: String)

val mockCards = listOf(
    BankCard("1", "Зарплатная", "45 200,00 ₽", CardRuby),
    BankCard("2", "Сберегательный", "150 000,50 ₽", CardSapphire),
    BankCard("3", "Кредитная", "10 000,00 ₽", CardGraphite)
)

val mockAds = listOf(
    AdBanner("Кредит наличными", "От 5.5% годовых", AdCreditBg),
    AdBanner("Ипотека", "Ставка от 4.7%", AdMortgageBg),
    AdBanner("Инвестиции", "Начните с 1000₽", AdInvestBg)
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BalanceSection(mockCards) }
            item { AdsSection(mockAds) }
            item { TipsSection(mockTips) }
        }
    }
}

@Composable
fun BalanceSection(cards: List<BankCard>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Ваши счета",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = BankOnContainerText
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
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
    var isBalanceVisible by remember { mutableStateOf(false) }
    var timerKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(timerKey) {
        if (isBalanceVisible) {
            delay(5000)
            isBalanceVisible = false
        }
    }

    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable {
                isBalanceVisible = true
                timerKey++
            },
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

            Box(contentAlignment = Alignment.CenterStart) {
                Text(
                    text = card.balance,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alpha(if (isBalanceVisible) 1f else 0f)
                )
                if (!isBalanceVisible) {
                    ParticleSpoiler(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ParticleSpoiler(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val random = java.util.Random(42) // Фиксированный seed для стабильности частиц
        val particleCount = 200
        repeat(particleCount) {
            drawCircle(
                color = Color.White.copy(alpha = random.nextFloat() * 0.5f + 0.2f),
                radius = (1..3).random(random).dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(
                    x = random.nextFloat() * size.width,
                    y = random.nextFloat() * size.height
                )
            )
        }
    }
}

fun IntRange.random(random: java.util.Random): Int =
    start + random.nextInt(endInclusive - start + 1)

@Composable
fun AdsSection(ads: List<AdBanner>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Спецпредложения",
            style = MaterialTheme.typography.titleLarge, // Увеличил шрифт для иерархии
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = BankOnContainerText // Наш темный цвет текста
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
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
    // Определяем цвет текста в зависимости от фона (можно сделать умнее, но пока вручную)
    val contentColor = when (ad.color) {
        AdCreditBg -> OnAdCredit
        AdMortgageBg -> OnAdMortgage
        AdInvestBg -> OnAdInvest
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .width(200.dp) // Чуть шире для лучшей читаемости
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ad.color)
            .clickable { /* Действие */ }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(
                text = ad.title,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ad.description,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun TipsSection(tips: List<Tip>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Советы дня",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            tips.forEach { tip ->
                TipItem(tip)
            }
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
