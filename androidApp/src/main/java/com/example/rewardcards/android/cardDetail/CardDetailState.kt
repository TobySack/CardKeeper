package com.example.rewardcards.android.cardDetail

import com.example.rewardcards.domain.time.DateTimeUtil
import kotlinx.datetime.LocalDateTime

data class CardDetailState(
    val cardName: String = "",
    val cardImage: String = "",
    val cardBarcode: String = "",
    val cardType: Int = -1,
    val cardColor: Long = 0xFFFFFFFF,
    val cardCreated: LocalDateTime = DateTimeUtil.now(),
    val cardNotes: String = ""
)