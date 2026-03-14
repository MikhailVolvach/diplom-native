package com.example.diplomnative.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diplomnative.data.BankCardEntity
import com.example.diplomnative.data.BankRepository
import com.example.diplomnative.data.NotificationEntity
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

    val allNotifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun performTransfer(
        fromCard: BankCardEntity,
        amount: Double,
        recipient: String,
        category: String = "Переводы"
    ) {
        viewModelScope.launch {
            val updatedCard = fromCard.copy(balance = fromCard.balance - amount)
            repository.updateCard(updatedCard)

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

            // Генерируем уведомление о списании
            repository.insertNotification(
                NotificationEntity(
                    title = "Списание",
                    message = "Перевод на сумму $amount ₽ выполнен успешно",
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun initMockData(cards: List<BankCardEntity>) {
        viewModelScope.launch {
            var count = 0;
            try {
                count = repository.getCardsCount()
            } catch (e: Exception) {
                println("Caught exception: ${e.message}")
            }

            if (count == 0) {
                repository.insertInitialCards(cards)
                
//                 Приветственное уведомление
                repository.insertNotification(
                    NotificationEntity(
                        title = "Добро пожаловать!",
                        message = "Ваше новое банковское приложение готово к работе.",
                        date = System.currentTimeMillis()
                    )
                )
            }
        }
    }

//    fun markAsRead(id: Long) {
//        viewModelScope.launch {
//            repository.markNotificationAsRead(id)
//        }
//    }
}
