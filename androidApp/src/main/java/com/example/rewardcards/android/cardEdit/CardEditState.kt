package com.example.rewardcards.android.cardEdit

data class CardEditState(
    val cardName: String = "",
    val cardImage: String = "",
    val cardBarcode: String = "",
    val cardColor: Long = 0xFFFFFFFF,
    val cardNotes: String = ""
)