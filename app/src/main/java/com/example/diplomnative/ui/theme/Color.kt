package com.example.diplomnative.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Мои кастомные цвета
val BankGreen = Color(0xFF155A43)

// 1. Дефолтная кнопка: Акцентный светло-зеленый или золотистый
// На темно-зеленом фоне отлично смотрится мягкий лаймовый или светлый "мятный"
val BankButtonPrimary = Color(0xFF94E2B2)

// 2. Нажатая кнопка: Чуть более темный оттенок акцентного
val BankButtonPressed = Color(0xFF6DCB91)

// 3. Цвет подложки (фон самого экрана): Очень светлый "грязный" белый
// Это создаст эффект глубины, когда зеленый контейнер лежит на нем
val BankScreenBackground = Color(0xFFF0F4F2)

val BankOnBackgroundText = Color(0xFF0D2B20)

// 4. Текст на зеленом контейнере: Бело-мятный (лучше, чем просто чисто белый)
val BankOnContainerText = Color(0xFFE0F2E9)

// 5. Вторичный акцент: Песочный/Золотистый (для иконок или важных уведомлений)
val BankAccentGold = Color(0xFFFFD700)

val BankGreen70 = BankGreen.copy(alpha = 0.8f)
val BankBackground = Color(0xFFF5F5F5) // Светло-серый фон для контраста
val BankDarkBackground = Color(0xFF0A2E22) // Более темный зеленый для темной темы

// 1. Глубокий синий (Sapphire - для дебетовых/привилегированных карт)
val CardSapphire = Color(0xFF1A3E59)
// 2. Темный графит (Anthracite - для кредитных карт или Premium)
val CardGraphite = Color(0xFF2B2D2F)
// 3. Благородный бордо (Ruby - акцентный вариант)
val CardRuby = Color(0xFF7B1D21)
// 4. Текст на кнопках/карточках (уже обсуждали, но для контекста)
val CardOnText = Color(0xFFE0F2E9)

// 1. Для Кредита (Мягкий золотисто-бежевый - символ достатка)
val AdCreditBg = Color(0xFFFFE0B2)
val OnAdCredit = Color(0xFF5D4037)

// 2. Для Ипотеки (Глубокий сине-зеленый - символ дома и спокойствия)
val AdMortgageBg = Color(0xFFC8E6C9)
val OnAdMortgage = Color(0xFF1B5E20)

// 3. Для Инвестиций (Светло-фиолетовый или лавандовый - символ роста и технологий)
val AdInvestBg = Color(0xFFE1BEE7)
val OnAdInvest = Color(0xFF4A148C)
