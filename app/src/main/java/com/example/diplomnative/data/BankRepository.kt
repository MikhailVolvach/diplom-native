package com.example.diplomnative.data

import kotlinx.coroutines.flow.Flow

class BankRepository(private val bankDao: BankDao) {
    val allCards: Flow<List<BankCardEntity>> = bankDao.getAllCards()
    val allTransactions: Flow<List<TransactionEntity>> = bankDao.getAllTransactions()
    val allNotifications: Flow<List<NotificationEntity>> = bankDao.getAllNotifications()

    suspend fun getCardsCount(): Int {
        try {
            return bankDao.getCardsCount()
        } catch (e: Exception) {
            println("getCardsCount exception: ${e.message}");
            return 0;
        }
    }

    suspend fun insertInitialCards(cards: List<BankCardEntity>) {
        bankDao.insertInitialCards(cards)
    }

    suspend fun updateCard(card: BankCardEntity) {
        bankDao.updateCard(card)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        bankDao.insertTransaction(transaction)
    }

    suspend fun deleteAllCards() {
        bankDao.deleteAllCards()
    }

    suspend fun insertNotification(notification: NotificationEntity) {
        bankDao.insertNotification(notification)
    }

//    suspend fun markNotificationAsRead(id: Long) {
//        bankDao.markAsRead(id)
//    }
}
