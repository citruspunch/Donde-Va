@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dondeva.presentation.scanning.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dondeva.R
import com.example.dondeva.data.impl.AccountServiceImpl
import com.example.dondeva.data.impl.StorageServiceImpl
import com.example.dondeva.domain.entity.GarbageItem
import com.example.dondeva.presentation.history.HistoryViewModel
import com.example.dondeva.presentation.scanning.data.TfLiteGarbageClassifier
import com.example.dondeva.presentation.scanning.domain.Classification
import kotlinx.coroutines.launch

@Composable
fun ScanningPage(
    onNavigateToLoginPage: () -> Unit,
    onNavigateToHistoryView: () -> Unit,
    onNavigateToHistoryItemView: (itemId: String) -> Unit,
) {
    val context = LocalContext.current
    val historyViewModel = viewModel {
        val accountService = AccountServiceImpl()
        HistoryViewModel(
            accountService = accountService,
            storageService = StorageServiceImpl(accountService),
        )
    }
    val classifier = remember {
        TfLiteGarbageClassifier(context.applicationContext)
    }
    val controller = remember {
        LifecycleCameraController(context.applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    val scope = rememberCoroutineScope()

    var savedGarbageItem by remember { mutableStateOf<GarbageItem?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }

    fun saveResult(result: Classification) = scope.launch {
        savedGarbageItem = historyViewModel.createNewHistoryEntry(result.type)
        isAnalyzing = false
    }

    fun analyzeImage() {
        isAnalyzing = true
        controller.takePicture(
            ContextCompat.getMainExecutor(context.applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = image.toBitmap().centerCrop(321, 321)
                    val result = classifier.classify(bitmap, image.imageInfo.rotationDegrees)
                    result.firstOrNull()?.let { saveResult(it) }
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Capture", "Error capturando imagen", exception)
                    isAnalyzing = false
                }
            },
        )
    }

    if (savedGarbageItem != null) ScanningResultAlertDialog(
        garbageType = savedGarbageItem!!.type,
        onDismissRequest = { savedGarbageItem = null },
        onSeeDetails = { onNavigateToHistoryItemView(savedGarbageItem!!.id) },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(onClick = onNavigateToHistoryView) {
                        Icon(
                            Icons.Outlined.History,
                            contentDescription = stringResource(R.string.history),
                        )
                    }
                    IconButton(onClick = onNavigateToLoginPage) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(controller, Modifier.fillMaxSize())
            Button(
                onClick = { if (isAnalyzing) Unit else analyzeImage() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp + innerPadding.calculateBottomPadding()),
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 16.dp),
            ) {
                if (isAnalyzing)
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                else
                    Icon(
                        Icons.Outlined.ImageSearch,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = if (isAnalyzing) "${stringResource(R.string.analyzing)}...".uppercase()
                    else stringResource(R.string.analyze).uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
            }

        }
    }

}

