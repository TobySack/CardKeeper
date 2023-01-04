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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
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
    val cardNotes = state.cardNotes.ifEmpty { "Add notes..." }
    val clipboardManager = LocalClipboardManager.current
    val formattedDate = remember(state.cardCreated) {
        DateTimeUtil.formatCardDate(state.cardCreated)
    }
    val formattedTime = remember(state.cardCreated) {
        DateTimeUtil.getTime(state.cardCreated)
    }

    val luminance: Double = (0.299 * Color(state.cardColor).red + 0.587 * Color(state.cardColor).green + 0.114 * Color(state.cardColor).blue) / 255
    val textColor = if (luminance > 0.002) Color(0, 0, 0) else Color(255, 255, 255)

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
                            painter = painterResource(id = Company.getCompanyLogo(state.cardName.lowercase().trim()).logo),
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
                            .width(2.dp)
                            .height(100.dp)
                            .alpha(0.25f)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.Black)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = state.cardName,
                            color = textColor,
                            fontSize = 22.sp
                        )
                        Text(
                            text = formattedDate,
                            color = textColor,
                            fontSize = 13.sp
                        )
                        Text(
                            text = formattedTime,
                            color = textColor,
                            fontSize = 13.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
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
                        BarcodeBrightness()
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
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                    .background(Color.White)
                    .padding(16.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(barcodeText))
                    }
            ) {
                AutoResizedText(
                    text = barcodeText,
                    style = MaterialTheme.typography.h4,
                    color = Color.Black
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
                            fontSize = 20.sp,
                            color = textColor
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                            .background(MaterialTheme.colors.surface)
                            .clickable {
                                navController.navigate(Screen.CardEdit.withArgs(cardId)) {
                                    navController.currentDestination?.let {
                                        popUpTo(it.id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Edit",
                        fontSize = 17.sp,
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 17.sp,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}