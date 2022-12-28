package com.example.rewardcards.data.local

import android.content.Context
import com.example.rewardcards.database.CardDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(CardDatabase.Schema, context, "card.db")
    }
}