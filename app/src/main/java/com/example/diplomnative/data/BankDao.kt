package com.example.diplomnative.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface BankDao {
    // Получить все карты
    @androidx.room.Query("SELECT * FROM bank_cards")
    fun getAllCards(): Flow<List<BankCardEntity>>

    // Получить историю операций
    @androidx.room.Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    // Обновить баланс карты (при переводе)
    @androidx.room.Update
    suspend fun updateCard(card: BankCardEntity)

    // Добавить операцию в историю
    @androidx.room.Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    // Инициализация начальными данными (вместо моков)
    @androidx.room.Insert
    suspend fun insertInitialCards(cards: List<BankCardEntity>)
}