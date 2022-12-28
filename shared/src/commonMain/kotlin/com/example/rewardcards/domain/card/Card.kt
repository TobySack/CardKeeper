package com.example.rewardcards.domain.card

import kotlinx.datetime.LocalDateTime

data class Card(
    val id: Long?,
    val name: String,
    val image: String,
    val barcode: String,
    val color: Long,
    val created: LocalDateTime,
    val notes: String
) {

}