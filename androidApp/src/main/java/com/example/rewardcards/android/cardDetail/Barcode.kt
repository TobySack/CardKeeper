package com.example.rewardcards.android.cardDetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Barcode(
    modifier: Modifier = Modifier,
    showProgress: Boolean = true,
    resolutionFactor: Int = 10,
    type: BarcodeType,
    value: String,
    width: Int = 100,
    height: Int = 100
) {
    val barcodeBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    // The launched effect will run every time the value changes. So, if the barcode changes,
    // the coroutine to get the bitmap will be started.
    LaunchedEffect(value) {
        scope.launch {
            withContext(Dispatchers.Default) {
                barcodeBitmap.value = try {
                    type.getImageBitmap(
                        width = width * resolutionFactor,
                        height = height * resolutionFactor,
                        value = value
                    )
                } catch (e: Exception) {
                    Log.e("ComposeBarcodes", "Invalid Barcode Format", e)
                    null
                }
            }
        }
    }

    // Contain the barcode in a box that matches the provided dimensions
    Box(modifier = modifier) {
        // If the barcode is not null, display it. If it is null, then the code hasn't
        // completed the draw in the background so show a progress spinner in place.
        barcodeBitmap.value?.let { barcode ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = BitmapPainter(barcode),
                contentDescription = value
            )
        } ?: run {
            if (showProgress) {
                CircularProgressIndicator()
            }
        }
    }
}