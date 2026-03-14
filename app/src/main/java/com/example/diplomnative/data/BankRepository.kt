package com.example.diplomnative.data

import kotlinx.coroutines.flow.Flow

class BankRepository(private val bankDao: BankDao) {
    val allCards: Flow<List<BankCardEntity>> = bankDao.getAllCards()
    val allTransactions: Flow<List<TransactionEntity>> = bankDao.getAllTransactions()

    suspend fun getCardsCount(): Int = bankDao.getCardsCount()

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
}
