package com.example.rewardcards.android.cardAdd

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CardAddScreen(
    navController: NavController,
    viewModel: CardAddViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val hasCardBeenSaved by viewModel.hasCardBeenSaved.collectAsState()
    val cameraActive by viewModel.isCameraActive.collectAsState()

    LaunchedEffect(key1 = hasCardBeenSaved) {
        if (hasCardBeenSaved) {
            navController.popBackStack()
        }
    }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::saveCard,
                backgroundColor = Color.Black
            ) {
                Text(
                    text = "Save Card",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(padding)
        ) {
            // Card name
            TransparentHintTextField(
                text = state.cardName,
                hint = "Enter a name",
                isHintVisible = state.isCardNameHintVisible,
                onValueChanged = viewModel::onCardNameChanged,
                onFocusedChanged = {
                    viewModel.onCardNameFocusedChanged(it.isFocused)
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 22.sp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            // Card Barcode or QR code
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                        ), RoundedCornerShape(10.dp)
                    )
                    .aspectRatio(1f)
                    .clipToBounds()
            ) {
                Text(
                    text = viewModel.getBarcode(),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    textAlign = TextAlign.Center
                )
                if (cameraActive) {
                    CameraPreview(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp)),
                        viewModel = viewModel
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            // Card Notes
            TransparentHintTextField(
                text = state.cardNotes,
                hint = "Enter any notes",
                isHintVisible = state.isCardNoteHintVisible,
                onValueChanged = viewModel::onCardNotesChanged,
                onFocusedChanged = {
                    viewModel.onCardNotesFocusedChanged(it.isFocused)
                },
                singleLine = false,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }
    }
}