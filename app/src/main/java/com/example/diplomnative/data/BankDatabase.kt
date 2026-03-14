package com.example.diplomnative.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BankCardEntity::class, TransactionEntity::class], version = 1)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
}