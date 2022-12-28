package com.example.rewardcards.data.card

import com.example.rewardcards.database.CardDatabase
import com.example.rewardcards.domain.card.Card
import com.example.rewardcards.domain.card.CardDataSource
import com.example.rewardcards.domain.time.DateTimeUtil

class SqlDelightCardDataSource(db: CardDatabase): CardDataSource {

    private val queries = db.cardQueries
    override suspend fun insertCard(card: Card) {
        queries.insertCard(
            id = card.id,
            name = card.name,
            image = card.image,
            barcode = card.barcode,
            color = card.color,
            created = DateTimeUtil.toEpochMillis(card.created),
            notes = card.notes
        )
    }

    override suspend fun getCardById(id: Long): Card? {
        return queries
            .getCardById(id)
            .executeAsOneOrNull()
            ?.toCard()
    }

    override suspend fun getAllCards(): List<Card> {
        return queries
            .getAllCards()
            .executeAsList()
            .map { it.toCard() }
    }

    override suspend fun deleteCardById(id: Long) {
        queries.deleteCard(id)
    }
}