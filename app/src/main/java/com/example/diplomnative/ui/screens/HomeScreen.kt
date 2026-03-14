package com.example.diplomnative.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.theme.*
import com.example.diplomnative.ui.viewmodel.BankViewModel
import kotlinx.coroutines.delay
import java.text.DecimalFormat

// UI модели (оставляем для обратной совместимости или маппинга)
data class BankCard(val id: String, val name: String, val balance: String, val color: Color)
data class AdBanner(val title: String, val description: String, val color: Color)
data class Tip(val title: String, val description: String)

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
fun HomeScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val cardEntities by viewModel.allCards.collectAsState()
    
    // Маппим Entity из базы в UI модель
    val cards = cardEntities.map { entity ->
        BankCard(
            id = entity.id.toString(),
            name = entity.name,
            balance = entity.balance.formatBalance(),
            color = Color(entity.colorHex.toULong().toInt())
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent // Фон берется из подложки в MainActivity
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { 
                if (cards.isNotEmpty()) {
                    BalanceSection(cards) 
                } else {
                    // Заглушка пока данные грузятся
                    Box(Modifier.fillMaxWidth().height(180.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = BankGreen)
                    }
                }
            }
            item { AdsSection(mockAds) }
            item { TipsSection(mockTips) }
        }
    }
}

// Хелпер для форматирования валюты
fun Double.formatBalance(): String {
    val formatter = DecimalFormat("#,###.00 ₽")
    return formatter.format(this).replace(",", " ")
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
        val random = java.util.Random(42)
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = BankOnContainerText
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
    val contentColor = when (ad.color) {
        AdCreditBg -> OnAdCredit
        AdMortgageBg -> OnAdMortgage
        AdInvestBg -> OnAdInvest
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ad.color)
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
            color = BankOnContainerText
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
                style = MaterialTheme.typography.bodyMedium,
                color = BankOnBackgroundText
            )
            Text(
                text = tip.description,
                style = MaterialTheme.typography.bodySmall,
                color = BankOnBackgroundText.copy(alpha = 0.7f)
            )
        }
    }
}
