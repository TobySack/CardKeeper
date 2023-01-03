package com.example.rewardcards.android.cardDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rewardcards.android.Company
import com.example.rewardcards.android.Screen
import com.example.rewardcards.android.cardAdd.getType
import com.example.rewardcards.domain.time.DateTimeUtil

@SuppressLint("RememberReturnType")
@Composable
fun CardDetailScreen(
    cardId: Long,
    navController: NavController,
    viewModel: CardDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val barcodeText = if (state.cardType <= -1) "Could not render a Barcode" else state.cardBarcode
    val cardNotes = state.cardNotes.ifEmpty { "None" }
    val formattedDate = remember(state.cardCreated) {
        DateTimeUtil.formatCardDate(state.cardCreated)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(Color(state.cardColor))
                    .padding(16.dp)
                    .height(100.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painterResource(id = Company.getCompanyLogo(state.cardName.lowercase()).logo),
                            contentDescription = "",
                            contentScale = ContentScale.Inside,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = state.cardName,
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formattedDate,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                        ), RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
                    )
                    .fillMaxWidth()
                    .clipToBounds()
                    .clip(RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp))
                    .background(Color.White)
            ) {
                if (state.cardType > -1) {
                    val foundBarcodeType = getType(state.cardType)
                    val barcodeValue = state.cardBarcode
                    if (foundBarcodeType.isValueValid(barcodeValue)) {
                        var barcodeWidth = 300
                        var barcodeHeight = 125
                        if (foundBarcodeType == BarcodeType.QR_CODE) {
                            barcodeWidth = 300
                            barcodeHeight = 300
                        }
                        Barcode(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            resolutionFactor = 10,
                            type = foundBarcodeType,
                            value = barcodeValue,
                            width = barcodeWidth,
                            height = barcodeHeight
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                    .background(MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled))
                    .height(50.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = barcodeText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = 4.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(state.cardColor))
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Notes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                            .background(MaterialTheme.colors.surface)
                    ) {
                        Text(
                            text = cardNotes,
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.CardEdit.withArgs(cardId)) {
                            navController.currentDestination?.let {
                                popUpTo(it.id) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Edit",
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.deleteCardById(cardId)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}