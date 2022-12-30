package com.example.rewardcards.android.cardAdd

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
class CardAddViewModel @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val cardName = savedStateHandle.getStateFlow("cardName", "")
    private val cardImage = savedStateHandle.getStateFlow("cardImage", "")
    private val cardBarcode = savedStateHandle.getStateFlow("cardBarcode", "No Barcode")
    private val cardType: StateFlow<Int> = savedStateHandle.getStateFlow("cardType", -1)
    private val cardColor = savedStateHandle.getStateFlow("cardColor", Card.generateRandomColor())
    private val cardNotes = savedStateHandle.getStateFlow("cardNotes", "")
    private val isCardNameFocused = savedStateHandle.getStateFlow("isCardNameFocused", false)
    private val isCardNotesFocused = savedStateHandle.getStateFlow("isCardNotesFocused", false)

    private val _hasCardBeenSaved = MutableStateFlow(false)
    val hasCardBeenSaved = _hasCardBeenSaved.asStateFlow()

    private val _isCameraActive = MutableStateFlow(true)
    val isCameraActive = _isCameraActive.asStateFlow()

    private var cardId: Long? = null

    val state = combine(
        cardName,
        cardImage,
        cardBarcode,
        cardType,
        cardColor,
        cardNotes,
        isCardNameFocused,
        isCardNotesFocused
    ) {
        name, image, barcode, type, color, notes, isNameFocused, isNotesFocused ->
        CardAddState(
            cardName = name,
            cardImage = image,
            cardBarcode = barcode,
            cardType = type,
            cardColor = color,
            cardNotes = notes,
            isCardNameHintVisible = name.isEmpty() && !isNameFocused,
            isCardNoteHintVisible = notes.isEmpty() && !isNotesFocused
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardAddState())

    init {
        savedStateHandle.get<Long>("cardId")?.let { cardId->
            if (cardId == -1L) {
                return@let
            }

            this.cardId = cardId

            viewModelScope.launch {
                cardDataSource.getCardById(cardId)?.let { card ->
                    savedStateHandle["cardName"] = card.name
                    savedStateHandle["cardImage"] = card.image
                    savedStateHandle["cardBarcode"] = card.barcode
                    savedStateHandle["cardType"] = card.type
                    savedStateHandle["cardNotes"] = card.notes
                    savedStateHandle["cardColor"] = card.color
                }
            }
        }
    }

    fun onCardNameChanged(text: String) {
        savedStateHandle["cardName"] = text
    }

    fun setBarcode(text: String) {
        savedStateHandle["cardBarcode"] = text
    }

    fun setType(type: Int) {
        savedStateHandle["cardType"] = type
    }

    fun onCardNotesChanged(text: String) {
        savedStateHandle["cardNotes"] = text
    }

    fun onCardNameFocusedChanged(isFocused: Boolean) {
        savedStateHandle["isCardNameFocused"] = isFocused
    }

    fun onCardNotesFocusedChanged(isFocused: Boolean) {
        savedStateHandle["isCardNotesFocused"] = isFocused
    }

    fun setCameraActiveState(state: Boolean) {
        _isCameraActive.value = state
    }

    fun saveCard() {
        viewModelScope.launch {
            if (cardName.value.isEmpty()) {
                return@launch
            }

            cardDataSource.insertCard(
                Card(
                    id = cardId,
                    name = cardName.value,
                    image = cardImage.value,
                    barcode = cardBarcode.value,
                    type = cardType.value,
                    color = cardColor.value,
                    created = DateTimeUtil.now(),
                    notes = cardNotes.value
                )
            )

            _hasCardBeenSaved.value = true
        }
    }

    private fun combine(
        flow: StateFlow<String>,
        flow2: StateFlow<String>,
        flow3: StateFlow<String>,
        flow4: StateFlow<Int>,
        flow5: StateFlow<Long>,
        flow6: StateFlow<String>,
        flow7: StateFlow<Boolean>,
        flow8: StateFlow<Boolean>,
        transform: suspend (String, String, String, Int, Long, String, Boolean, Boolean) -> CardAddState
    ): Flow<CardAddState> = combine(
        combine(flow, flow2, ::Pair),
        combine(flow3, flow4, flow5, ::Triple),
        combine(flow6, flow7, flow8, ::Triple)
    ) {
        t1, t2, t3 ->
        transform(
            t1.first,
            t1.second,
            t2.first,
            t2.second,
            t2.third,
            t3.first,
            t3.second,
            t3.third
        )
    }
}