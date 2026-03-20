package com.example.diplomnative.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.diplomnative.R
import com.example.diplomnative.data.BankCardEntity
import com.example.diplomnative.ui.theme.*
import com.example.diplomnative.ui.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(viewModel: BankViewModel, onBack: () -> Unit = {}) {
    val cards by viewModel.allCards.collectAsState()
    var selectedCard by remember { mutableStateOf<BankCardEntity?>(null) }
    
    var transferType by remember { mutableStateOf(TransferType.CARD) }
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Инициализация выбранной карты
    LaunchedEffect(cards) {
        if (selectedCard == null && cards.isNotEmpty()) {
            selectedCard = cards.first()
        }
    }

    LaunchedEffect(transferType) {
        recipient = ""
    }

    val recipientMaxLength = when (transferType) {
        TransferType.CARD -> 16
        TransferType.PHONE -> 10
        TransferType.ACCOUNT -> 20
    }

    val recipientPlaceholder = when (transferType) {
        TransferType.CARD -> "0000 0000 0000 0000"
        TransferType.PHONE -> "+7 (___) ___-__-__"
        TransferType.ACCOUNT -> "0000 0000 0000 0000 0000"
    }

    val visualTransformation = when (transferType) {
        TransferType.CARD -> CardNumberVisualTransformation()
        TransferType.PHONE -> PhoneVisualTransformation()
        TransferType.ACCOUNT -> AccountNumberVisualTransformation()
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BankGreen,
        unfocusedBorderColor = BankGreen.copy(alpha = 0.3f),
        focusedLabelColor = BankGreen,
        cursorColor = BankGreen,
        focusedTextColor = BankOnBackgroundText,
        unfocusedTextColor = BankOnBackgroundText
    )

    Scaffold(
        containerColor = BankScreenBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Перевод средств",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BankOnBackgroundText
            )

            // Секция выбора карты
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Списать с",
                    style = MaterialTheme.typography.titleSmall,
                    color = BankOnBackgroundText.copy(alpha = 0.6f)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(cards) { card ->
                        val isSelected = selectedCard?.id == card.id
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(card.colorHex.toULong().toInt()))
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) BankAccentGold else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedCard = card }
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
                                Text(card.name, color = Color.White, style = MaterialTheme.typography.labelMedium)
                                Text(card.balance.formatBalance(), color = Color.White, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Выбор типа перевода
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                TransferType.entries.forEachIndexed { index, type ->
                    val label = when(type) {
                        TransferType.CARD -> "Карта"
                        TransferType.PHONE -> "Телефон"
                        TransferType.ACCOUNT -> "Счет"
                    }
                    SegmentedButton(
                        selected = transferType == type,
                        onClick = { transferType = type },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = TransferType.entries.size),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = BankGreen,
                            activeContentColor = Color.White,
                            inactiveContentColor = BankGreen
                        ),
                        icon = {
                            if (transferType == type) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ok),
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        }
                    ) {
                        Text(label)
                    }
                }
            }

            // Ввод реквизитов
            OutlinedTextField(
                value = recipient,
                onValueChange = { if (it.length <= recipientMaxLength && it.all { c -> c.isDigit() }) recipient = it },
                label = { Text(when (transferType) {
                    TransferType.CARD -> "Номер карты получателя"
                    TransferType.PHONE -> "Номер телефона"
                    TransferType.ACCOUNT -> "Номер счета"
                }) },
                placeholder = if (isFocused) { { Text(recipientPlaceholder) } } else null,
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                interactionSource = interactionSource,
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp)
            )

            // Сумма
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { c -> c.isDigit() || c == '.' }) amount = it },
                label = { Text("Сумма") },
                suffix = { Text("₽") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = textFieldColors,
                shape = RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    selectedCard?.let { card ->
                        viewModel.performTransfer(
                            fromCard = card,
                            amount = amountDouble,
                            recipient = when(transferType) {
                                TransferType.CARD -> recipient.chunked(4).joinToString(" ")
                                TransferType.PHONE -> "+7 $recipient"
                                else -> recipient
                            }
                        )
                        // Очистка полей после успешного нажатия (в реальности после подтверждения)
                        recipient = ""
                        amount = ""
                        comment = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = recipient.length == recipientMaxLength && amount.toDoubleOrNull() != null && selectedCard != null,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BankGreen)
            ) {
                Text("Отправить перевод", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }

}

// Visual transformations (оставляем те же)
class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        for (i in text.text.indices) {
            out += text.text[i]
            if (i % 4 == 3 && i != 15) out += " "
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 8) return offset + 1
                if (offset <= 12) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        if (text.text.isNotEmpty()) {
            out = "+7 ("
            for (i in text.text.indices) {
                out += text.text[i]
                if (i == 2) out += ") "
                if (i == 5 || i == 7) out += "-"
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                if (offset <= 3) return offset + 4
                if (offset <= 6) return offset + 6
                if (offset <= 8) return offset + 7
                if (offset <= 10) return offset + 8
                return 18
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return 0
                if (offset <= 7) return offset - 4
                if (offset <= 11) return offset - 6
                if (offset <= 14) return offset - 7
                if (offset <= 18) return offset - 8
                return 10
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

enum class TransferType(
    name: String
) {
    CARD(name="card"),
    PHONE(name="phone"),
    ACCOUNT(name="account")
}

class AccountNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        for (i in text.text.indices) {
            out += text.text[i]
            if (i % 4 == 3 && i != 19) out += " "
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 8) return offset + 1
                if (offset <= 12) return offset + 2
                if (offset <= 16) return offset + 3
                if (offset <= 20) return offset + 4
                return 24
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                if (offset <= 24) return offset - 4
                return 20
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
