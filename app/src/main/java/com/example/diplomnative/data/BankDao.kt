package com.example.diplomnative.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {
    @Query("SELECT * FROM bank_cards")
    fun getAllCards(): Flow<List<BankCardEntity>>

    @Query("SELECT COUNT(*) FROM bank_cards")
    suspend fun getCardsCount(): Int

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Update
    suspend fun updateCard(card: BankCardEntity)

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInitialCards(cards: List<BankCardEntity>)

    @Query("DELETE FROM bank_cards")
    suspend fun deleteAllCards()
}
