package com.example.rewardcards.android.cardList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardListScreen(
    navController: NavController,
    viewModel: CardListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.loadCards()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("cardAdd")
                },
                backgroundColor = Color.Black
            ) {
                Text(
                    text = "Add Card",
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HideableSearchTextField(
                    text = state.searchCards,
                    isSearchActive = state.isSearchActive,
                    onTextChanged = viewModel::onSearchTextChange,
                    onSearchClick = viewModel::onToggleSearch,
                    onCloseClick = viewModel::onToggleSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )

                this@Column.AnimatedVisibility(
                    visible = !state.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "My Cards",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            }
            
            LazyVerticalGrid(
                columns = GridCells.Adaptive(175.dp),
                content = {
                    items(state.cards) { card ->
                        CardItem(
                            card = card,
                            backgroundColor = Color(card.color),
                            onCardClick = {
                                navController.navigate("cardDetail/${card.id}")
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .animateItemPlacement()
                        )
                    }
                }
            )
        }
    }
}