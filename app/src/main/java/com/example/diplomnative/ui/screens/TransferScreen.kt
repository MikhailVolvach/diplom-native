package com.example.diplomnative.ui.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Выберите тип перевода",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.surface
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = transferType == TransferType.CARD,
                    onClick = { transferType = TransferType.CARD },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                    colors = SegmentedButtonDefaults.colors(
//                        inactiveContainerColor =
                    )
                ) {
                    Text("Карта", color = MaterialTheme.colorScheme.surface)
                }
                SegmentedButton(
                    selected = transferType == TransferType.PHONE,
                    onClick = { transferType = TransferType.PHONE },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
                ) {
                    Text("Телефон", color = MaterialTheme.colorScheme.surface)
                }
                SegmentedButton(
                    selected = transferType == TransferType.ACCOUNT,
                    onClick = { transferType = TransferType.ACCOUNT },
                    shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
                ) {
                    Text("Счет", color = MaterialTheme.colorScheme.surface)
                }
            }

            OutlinedTextField(
                value = recipient,
                onValueChange = {
                    if (it.length <= recipientMaxLength && it.all { char -> char.isDigit() }) {
                        recipient = it
                    }
                },
                label = {
                    Text(
                        when (transferType) {
                            TransferType.CARD -> "Номер карты"
                            TransferType.PHONE -> "Номер телефона"
                            TransferType.ACCOUNT -> "Номер счета"
                        }
                    )
                },
                placeholder = if (isFocused) {
                    { Text(recipientPlaceholder) }
                } else null,
                visualTransformation = visualTransformation,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                interactionSource = interactionSource
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Сумма перевода") },
                suffix = { Text("₽") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Комментарий (необязательно)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* TODO: Выполнить перевод */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = recipient.length == recipientMaxLength && amount.isNotBlank()
            ) {
                Text("Перевести", style = MaterialTheme.typography.titleMedium)
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
