package com.example.rewardcards.di

import com.example.rewardcards.data.card.SqlDelightCardDataSource
import com.example.rewardcards.data.local.DatabaseDriverFactory
import com.example.rewardcards.database.CardDatabase
import com.example.rewardcards.domain.card.CardDataSource

class DatabaseModule {
    private val factory by lazy { DatabaseDriverFactory() }
    val cardDataSource: CardDataSource by lazy {
        SqlDelightCardDataSource(CardDatabase(factory.createDriver()))
    }
}