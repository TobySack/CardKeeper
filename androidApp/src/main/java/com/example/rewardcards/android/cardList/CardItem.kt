package com.example.rewardcards.android.cardList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rewardcards.android.Company
import com.example.rewardcards.domain.card.Card

@Composable
fun CardItem(
    card: Card,
    backgroundColor: Color,
    onCardClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .clickable { onCardClick() }
            .padding(8.dp)
            .aspectRatio(1.586f / 1f)
    ) {
        Row {
            Image(
                painter = painterResource(id = Company.getCompanyLogo(card.name.lowercase().trim()).logo),
                contentDescription = "",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}