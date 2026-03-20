package com.example.diplomnative.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bank_cards")
data class BankCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val balance: Double,
    val colorHex: Long
)

@Entity(
    tableName = "transactions",
//    indices = [Index(value = ["date"]), Index(value = ["cardId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardId: Long,
    val title: String,
    val category: String,
    val amount: Double,
    val date: Long, // Timestamp
    val isIncome: Boolean,
    val iconName: String
)

@Entity(
    tableName = "notifications",
//    indices = [Index(value = ["date"])]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val date: Long,
    val isRead: Boolean = false
)
