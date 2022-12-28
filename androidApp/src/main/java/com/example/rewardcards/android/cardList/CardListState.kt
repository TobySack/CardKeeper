package com.example.rewardcards.android.cardList

import com.example.rewardcards.domain.card.Card

data class CardListState(
    val cards: List<Card> = emptyList(),
    val searchCards: String = "",
    val isSearchActive: Boolean = false
)