package com.example.diplomnative.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.diplomnative.ui.theme.CardGraphite
import com.example.diplomnative.ui.theme.CardRuby
import com.example.diplomnative.ui.theme.CardSapphire
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.toArgb

@Database(
    entities = [BankCardEntity::class, TransactionEntity::class, NotificationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao

    companion object {
        @Volatile
        private var INSTANCE: BankDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BankDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BankDatabase::class.java,
                    "bank_database"
                )
                    .addCallback(BankDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class BankDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    val bankDao = database.bankDao()
                    
                    // Предзаполнение картами
                    bankDao.insertInitialCards(
                        listOf(
                            BankCardEntity(name = "Зарплатная", balance = 45200.0, colorHex = CardRuby.toArgb().toLong()),
                            BankCardEntity(name = "Сберегательный", balance = 150000.5, colorHex = CardSapphire.toArgb().toLong()),
                            BankCardEntity(name = "Кредитная", balance = 10000.0, colorHex = CardGraphite.toArgb().toLong())
                        )
                    )

                    // Приветственное уведомление
                    bankDao.insertNotification(
                        NotificationEntity(
                            title = "Добро пожаловать!",
                            message = "Ваше новое банковское приложение готово к работе.",
                            date = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }
}
