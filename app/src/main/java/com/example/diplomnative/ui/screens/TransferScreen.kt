package com.example.diplomnative.ui.screens

import androidx.compose.animation.core.copy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diplomnative.ui.theme.BankGreen
import com.example.diplomnative.ui.theme.BankOnBackgroundText
import com.example.diplomnative.ui.theme.BankOnContainerText
import com.example.diplomnative.ui.theme.BankScreenBackground
import com.example.diplomnative.ui.theme.DiplomNativeTheme

enum class TransferType {
    CARD, PHONE, ACCOUNT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(onBack: () -> Unit = {}) {
    var transferType by remember { mutableStateOf(TransferType.CARD) }
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Очищаем ввод при смене типа перевода
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

    // Настройка цветов для текстовых полей
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BankGreen,
        unfocusedBorderColor = BankGreen.copy(alpha = 0.3f),
        focusedLabelColor = BankGreen,
        cursorColor = BankGreen,
        focusedTextColor = BankOnBackgroundText,
        unfocusedTextColor = BankOnBackgroundText
    )

    Scaffold(
        containerColor = BankScreenBackground // Устанавливаем фон экрана
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp), // Чуть больше отступы для "воздуха"
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Перевод средств",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = BankOnBackgroundText
            )

            // Стилизованный переключатель
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                val types = listOf(
                    TransferType.CARD to "Карта",
                    TransferType.PHONE to "Телефон",
                    TransferType.ACCOUNT to "Счет"
                )

                types.forEachIndexed { index, (type, label) ->
                    SegmentedButton(
                        selected = transferType == type,
                        onClick = { transferType = type },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 3),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = BankGreen,
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.Transparent,
                            inactiveContentColor = BankGreen
                        )
                    ) {
                        Text(label, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Поле ввода получателя
            OutlinedTextField(
                value = recipient,
                onValueChange = {
                    if (it.length <= recipientMaxLength && it.all { char -> char.isDigit() }) {
                        recipient = it
                    }
                },
                label = { Text(when (transferType) {
                    TransferType.CARD -> "Номер карты"
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

            // Поле ввода суммы
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сумма перевода") },
                suffix = { Text("₽", fontWeight = FontWeight.Bold) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = textFieldColors,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            // Поле комментария
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Комментарий (опционально)") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Главная кнопка перевода
            Button(
                onClick = { /* Выполнить перевод */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = recipient.length == recipientMaxLength && amount.isNotBlank(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BankGreen,
                    contentColor = Color.White,
                    disabledContainerColor = BankGreen.copy(alpha = 0.3f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "Отправить перевод",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

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
        // Input: 9991234567 (10 digits)
        // Output: +7 (999) 123-45-67
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

@Preview(showBackground = true)
@Composable
fun TransferScreenPreview() {
    DiplomNativeTheme {
        TransferScreen()
    }
}
