package com.example.rewardcards.android.cardEdit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CardEditScreen(
    cardId: Long,
    navController: NavController,
    viewModel: CardEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val hasCardBeenUpdated by viewModel.hasCardBeenUpdated.collectAsState()

    LaunchedEffect(key1 = hasCardBeenUpdated) {
        if (hasCardBeenUpdated) {
            viewModel.updateSaved(false)
            navController.navigate("cardDetail/${cardId}") {
                popUpTo("cardDetail/${cardId}") {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::updateCard,
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Card",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
        ) {
            TextField(
                value = state.cardName,
                onValueChange = { viewModel.onCardNameChanged(it) },
                label = { Text(text = "Name") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.cardBarcode,
                onValueChange = { viewModel.onCardBarcodeChanged(it) },
                label = { Text(text = "Barcode") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = state.cardNotes,
                onValueChange = { viewModel.onCardNotesChanged(it) },
                label = { Text(text = "Notes") },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}