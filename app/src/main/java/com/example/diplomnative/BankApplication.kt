package com.example.diplomnative

import android.app.Application
import androidx.room.Room
import com.example.diplomnative.data.BankDatabase
import com.example.diplomnative.data.BankRepository

class BankApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this,
            BankDatabase::class.java,
            "bank_database"
        ).build()
    }

    val repository by lazy {
        BankRepository(database.bankDao())
    }
}
