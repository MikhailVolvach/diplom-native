package com.example.diplomnative.data

import kotlinx.coroutines.flow.Flow

class BankRepository(private val bankDao: BankDao) {
    val allCards: Flow<List<BankCardEntity>> = bankDao.getAllCards()
    val allTransactions: Flow<List<TransactionEntity>> = bankDao.getAllTransactions()

    suspend fun insertInitialCards(cards: List<BankCardEntity>) {
        bankDao.insertInitialCards(cards)
    }

    suspend fun performTransfer(
        fromCardId: Long,
        amount: Double,
        recipient: String,
        title: String,
        category: String,
        iconName: String
    ) {
        // В реальном приложении это должно быть в @Transaction
        // Но для простоты реализуем здесь или в DAO
    }

    suspend fun updateCard(card: BankCardEntity) {
        bankDao.updateCard(card)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        bankDao.insertTransaction(transaction)
    }
}
