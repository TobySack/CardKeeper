package com.example.rewardcards.data.local

import com.example.rewardcards.database.CardDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(CardDatabase.Schema, "card.db")
    }
}