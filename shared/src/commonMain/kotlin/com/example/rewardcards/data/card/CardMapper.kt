package com.example.rewardcards.data.card

import com.example.rewardcards.domain.card.Card
import database.CardEntity
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun CardEntity.toCard(): Card {
    return Card(
        id = id,
        name = name,
        image = image,
        barcode = barcode,
        color = color,
        created = Instant
            .fromEpochMilliseconds(created)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        notes = notes
    )
}