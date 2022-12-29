package com.example.rewardcards.android.cardAdd

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    viewModel: CardAddViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                coroutineScope.launch {
                    startCamera(context, previewView, lifecycleOwner, viewModel)
                }

                previewView
            }
        )

        IconButton(
            modifier = Modifier.padding(16.dp),
            onClick = { /* TODO */ },
            content = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Take picture",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color.Black, CircleShape)
                )
            }
        )
    }
}

@SuppressLint("ClickableViewAccessibility")
private fun startCamera(context: Context, previewView: PreviewView, lifecycleOwner: LifecycleOwner, viewModel: CardAddViewModel) {
    val cameraController = LifecycleCameraController(context)
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)

    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        MlKitAnalyzer(
            listOf(barcodeScanner),
            COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(context)
        ) { result: MlKitAnalyzer.Result? ->
            val barcodeResults = result?.getValue(barcodeScanner)
            if ((barcodeResults == null) ||
                (barcodeResults.size == 0) ||
                (barcodeResults.first() == null)
            ) {
                previewView.overlay.clear()
                previewView.setOnTouchListener { _, _ -> false } //no-op
                return@MlKitAnalyzer
            }

            if (barcodeResults.isNotEmpty()) {
                viewModel.setCameraActiveState(false)
                viewModel.setBarcode(barcodeResults[0].rawValue.toString())
            }
        }
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}