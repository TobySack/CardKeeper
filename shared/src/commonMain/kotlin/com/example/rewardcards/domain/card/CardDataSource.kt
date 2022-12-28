package com.example.rewardcards.domain.card

interface CardDataSource {
    suspend fun insertCard(card: Card)
    suspend fun getCardById(id: Long): Card?
    suspend fun getAllCards(): List<Card>
    suspend fun deleteCardById(id: Long)
}