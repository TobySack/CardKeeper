package com.example.rewardcards.android.cardList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rewardcards.domain.card.Card
import com.example.rewardcards.domain.time.DateTimeUtil

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
            .padding(16.dp)
            .aspectRatio(1.586f / 1f)
    ) {
        Row {
            Text(
                text = card.name,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
    }
}

@Preview
@Composable
fun CardItemView() {
    CardItem(
        card = Card(-1, "Burger King", "Test", "23123123", 0xFFFFFFFF, DateTimeUtil.now(), "Test"),
        backgroundColor = Color(0xFFFFFFFF),
        onCardClick = {},
        modifier = Modifier
            .padding(16.dp)
    )
}