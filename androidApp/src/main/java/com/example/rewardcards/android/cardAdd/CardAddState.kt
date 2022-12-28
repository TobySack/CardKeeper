package com.example.rewardcards.android.cardAdd

data class CardAddState(
    val cardName: String = "",
    val cardImage: String = "",
    val cardBarcode: String = "",
    val cardColor: Long = 0xFFFFFFFF,
    val cardNotes: String = "",
    val isCardNameHintVisible: Boolean = false,
    val isCardNoteHintVisible: Boolean = false
)