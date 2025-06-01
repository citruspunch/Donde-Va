package com.example.dondeva.presentation.scanning.ui

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.dondeva.presentation.scanning.data.TfLiteGarbageClassifier
import com.example.dondeva.presentation.scanning.domain.Classification

@Composable
fun ScanningPage(onNavigateToLoginPage: () -> Unit){
    val context = LocalContext.current
    var classifications by remember {
        mutableStateOf(emptyList<Classification>())
    }
    val classifier = remember {
        TfLiteGarbageClassifier(context.applicationContext)
    }

    val controller = remember {
        LifecycleCameraController(context.applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CameraPreview(controller, Modifier.fillMaxSize())
        HeaderScanningPage(onNavigateToLoginPage)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {


            Button(onClick = {
                controller.takePicture(
                    ContextCompat.getMainExecutor(context.applicationContext),
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            val bitmap = image
                                .toBitmap()
                                .centerCrop(321, 321)
                            val result = classifier.classify(bitmap, image.imageInfo.rotationDegrees)
                            classifications = result
                            image.close()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("Capture", "Error capturando imagen", exception)
                        }
                    }
                )

            }) {
                Text("Analizar")
            }

            Spacer(modifier = Modifier.height(12.dp))

            classifications.forEach {
                Text(
                    text = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

    }

}

@Composable
fun HeaderScanningPage(onNavigateToLoginPage: () -> Unit){
    Row(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ){
        Text(
            text = "¿Dónde va?",
            color = Color.Blue,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        IconButton(modifier = Modifier.height(50.dp).width(50.dp), onClick = { onNavigateToLoginPage() }){
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                modifier = Modifier.height(50.dp).width(50.dp),
                tint = Color.Red
            )
        }
    }
}