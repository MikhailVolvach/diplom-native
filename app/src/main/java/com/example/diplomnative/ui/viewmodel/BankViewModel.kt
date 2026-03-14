package com.example.diplomnative.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplomnative.data.BankCardEntity
import com.example.diplomnative.data.BankRepository
import com.example.diplomnative.data.TransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BankViewModel(private val repository: BankRepository) : ViewModel() {

    val allCards: StateFlow<List<BankCardEntity>> = repository.allCards
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun performTransfer(
        fromCard: BankCardEntity,
        amount: Double,
        recipient: String,
        category: String = "Переводы"
    ) {
        viewModelScope.launch {
            // 1. Обновляем баланс карты
            val updatedCard = fromCard.copy(balance = fromCard.balance - amount)
            repository.updateCard(updatedCard)

            // 2. Добавляем запись в историю
            val transaction = TransactionEntity(
                cardId = fromCard.id,
                title = "Перевод $recipient",
                category = category,
                amount = amount,
                date = System.currentTimeMillis(),
                isIncome = false,
                iconName = "Send" // Имя иконки для маппинга
            )
            repository.insertTransaction(transaction)
        }
    }

    // Метод для инициализации данных (если база пуста)
    fun initMockData(cards: List<BankCardEntity>) {
        viewModelScope.launch {
            repository.insertInitialCards(cards)
        }
    }
}
