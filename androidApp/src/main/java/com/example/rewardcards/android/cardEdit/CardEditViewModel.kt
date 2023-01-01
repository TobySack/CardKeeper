package com.example.rewardcards.android.cardEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rewardcards.domain.card.Card
import com.example.rewardcards.domain.card.CardDataSource
import com.example.rewardcards.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardEditViewModel @Inject constructor(
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
    private val cardCreated = _cardCreated.asStateFlow()

    private val _hasCardBeenUpdated = MutableStateFlow(false)
    val hasCardBeenUpdated = _hasCardBeenUpdated.asStateFlow()

    val state = combine(
        cardName,
        cardImage,
        cardBarcode,
        cardColor,
        cardNotes
    ) {
        name, image, barcode, color, notes ->
        CardEditState(
            cardName = name,
            cardImage = image,
            cardBarcode = barcode,
            cardColor = color,
            cardNotes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardEditState())

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

    fun onCardNameChanged(text: String) {
        savedStateHandle["cardName"] = text
    }

    fun onCardImageChanged(text: String) {
        savedStateHandle["cardImage"] = text
    }

    fun onCardBarcodeChanged(text: String) {
        savedStateHandle["cardBarcode"] = text
    }

    fun onCardColorChanged(color: Long) {
        savedStateHandle["cardColor"] = color
    }

    fun onCardNotesChanged(text: String) {
        savedStateHandle["cardNotes"] = text
    }

    fun updateSaved(saved: Boolean) {
        _hasCardBeenUpdated.value = saved
    }

    fun updateCard() {
        viewModelScope.launch {
            if (cardName.value.isEmpty()) {
                // TODO: Maybe at least have a Toast message pop up with error
                return@launch
            }

            cardDataSource.insertCard(
                Card(
                    id = existingCardId,
                    name = cardName.value,
                    image = cardImage.value,
                    barcode = cardBarcode.value,
                    type = cardType.value,
                    color = cardColor.value,
                    created = cardCreated.value,
                    notes = cardNotes.value
                )
            )
            _hasCardBeenUpdated.value = true
        }
    }
}

private fun combine(
    flow: StateFlow<String>,
    flow2: StateFlow<String>,
    flow3: StateFlow<String>,
    flow4: StateFlow<Long>,
    flow5: StateFlow<String>,
    transform: suspend (String, String, String, Long, String) -> CardEditState
): Flow<CardEditState> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, ::Pair)
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second
    )
}