package com.example.rewardcards.android.cardDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rewardcards.domain.card.Card
import com.example.rewardcards.domain.card.CardDataSource
import com.example.rewardcards.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val cardName = savedStateHandle.getStateFlow("cardName", "")
    private val cardImage = savedStateHandle.getStateFlow("cardImage", "")
    private val cardBarcode = savedStateHandle.getStateFlow("cardBarcode", "No Barcode")
    private val cardType = savedStateHandle.getStateFlow("cardType", -1)
    private val cardColor = savedStateHandle.getStateFlow("cardColor", Card.generateRandomColor())
    private val cardNotes = savedStateHandle.getStateFlow("cardNotes", "")

    private var existingCardId: Long? = null

    // Can't store LocalDateTime in the saved state
    private val _cardCreated = MutableStateFlow(DateTimeUtil.now())
    val cardCreated = _cardCreated.asStateFlow()


    val state = combine(
        cardName,
        cardImage,
        cardBarcode,
        cardType,
        cardColor,
        cardCreated,
        cardNotes
    ) {
        name, image, barcode, type, color, created, notes ->
        CardDetailState(
            cardName = name,
            cardImage = image,
            cardBarcode = barcode,
            cardType = type,
            cardColor = color,
            cardCreated = created,
            cardNotes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailState())

    init {
        savedStateHandle.get<Long>("cardId")?.let { existingCardId ->
            if (existingCardId == -1L) {
                return@let
            }

            this.existingCardId = existingCardId

            viewModelScope.launch {
                cardDataSource.getCardById(existingCardId)?.let { card ->
                    savedStateHandle["cardName"] = card.name
                    savedStateHandle["cardImage"] = card.image
                    savedStateHandle["cardBarcode"] = card.barcode
                    savedStateHandle["cardType"] = card.type
                    savedStateHandle["cardColor"] = card.color
                    savedStateHandle["cardNotes"] = card.notes
                    _cardCreated.value = card.created
                }
            }
        }
    }

    fun deleteCardById(id: Long) {
        viewModelScope.launch {
            cardDataSource.deleteCardById(id)
        }
    }
}

private fun combine(
    flow: StateFlow<String>,
    flow2: StateFlow<String>,
    flow3: StateFlow<String>,
    flow4: StateFlow<Int>,
    flow5: StateFlow<Long>,
    flow6: StateFlow<LocalDateTime>,
    flow7: StateFlow<String>,
    transform: suspend (String, String, String, Int, Long, LocalDateTime, String) -> CardDetailState
): Flow<CardDetailState> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, ::Pair),
    combine(flow6, flow7, ::Pair)
) { t1, t2, t3 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t3.first,
        t3.second
    )
}