package com.example.diplomnative.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BankCardEntity::class,
        TransactionEntity::class,
        NotificationEntity::class
    ],
    version = 2,
//    exportSchema = false
)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
}