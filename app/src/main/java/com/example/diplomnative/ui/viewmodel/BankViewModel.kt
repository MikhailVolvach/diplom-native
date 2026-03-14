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
            // 1. Обновляем баланс в объекте и БД
            val updatedCard = fromCard.copy(balance = fromCard.balance - amount)
            repository.updateCard(updatedCard)

            // 2. Создаем запись в истории
            val transaction = TransactionEntity(
                cardId = fromCard.id,
                title = "Перевод $recipient",
                category = category,
                amount = amount,
                date = System.currentTimeMillis(),
                isIncome = false,
                iconName = "Send"
            )
            repository.insertTransaction(transaction)
        }
    }

    // Инициализация только если база действительно пуста
    fun initMockData(cards: List<BankCardEntity>) {
        viewModelScope.launch {
            val count = repository.getCardsCount()
            if (count == 0) {
                repository.insertInitialCards(cards)
            }
        }
    }
    
    // Метод для ручной очистки (если нужно сбросить состояние)
    fun resetDatabase() {
        viewModelScope.launch {
            repository.deleteAllCards()
            // Здесь можно также добавить удаление транзакций, если нужно
        }
    }
}
