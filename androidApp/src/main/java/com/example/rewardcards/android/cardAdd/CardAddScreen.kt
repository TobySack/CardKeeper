package com.example.rewardcards.android.cardAdd

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.simonsickle.compose.barcodes.Barcode
import com.simonsickle.compose.barcodes.BarcodeType

@Composable
fun CardAddScreen(
    navController: NavController,
    viewModel: CardAddViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val hasCardBeenSaved by viewModel.hasCardBeenSaved.collectAsState()
    val isCameraActive by viewModel.isCameraActive.collectAsState()
    val barcodeText = state.cardBarcode

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
                    text = barcodeText,
                    modifier = Modifier
                        .padding(8.dp, 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (isCameraActive) {
                    CameraPreview(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp)),
                        viewModel = viewModel
                    )
                } else {
                    // Camera isn't active
                    if (barcodeText.isEmpty()) {
                        // Barcode is empty so user didn't give permissions for the camera. Would want to let them manually enter the text
                    } else {
                        // Camera isn't active & we have text. Let user have option to retry
                        val foundBarcodeType = getType(state.cardType)
                        if (foundBarcodeType.isValueValid(barcodeText)) {
                            Barcode(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .aspectRatio(16f / 9f)
                                    .width(250.dp)
                                    .height(250.dp),
                                resolutionFactor = 10,
                                type = foundBarcodeType,
                                value = barcodeText
                            )
                        } else {
                            Text("Could not render a Barcode")
                        }

                        Button(
                            onClick = { viewModel.setCameraActiveState(true) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .padding(8.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Text(
                                text = "Try Again",
                                color = Color.White,
                                modifier = Modifier
                                    .padding(8.dp)
                            )
                        }
                    }
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