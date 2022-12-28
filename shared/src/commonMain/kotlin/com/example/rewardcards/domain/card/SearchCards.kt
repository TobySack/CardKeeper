package com.example.rewardcards.domain.card

import com.example.rewardcards.domain.time.DateTimeUtil

class SearchCards {
    fun execute(cards: List<Card>, query: String): List<Card> {
        if (query.isBlank()) {
            return cards
        }

        return cards.filter {
            it.name.trim().lowercase().contains(query.lowercase()) ||
                    it.barcode.trim().lowercase().contains(query.lowercase())
        }.sortedBy {
            DateTimeUtil.toEpochMillis(it.created)
        }
    }
}