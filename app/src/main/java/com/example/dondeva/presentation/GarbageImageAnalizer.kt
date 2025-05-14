package com.example.dondeva.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.dondeva.domain.Classification
import com.example.dondeva.domain.GarbageClassifier

class GarbageImageAnalizer(
    private val classifier: GarbageClassifier,
    private val onResults: (List<Classification>) -> Unit
): ImageAnalysis.Analyzer {

    private var frameSkipCounter = 0


    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0){
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321)

            val results = classifier.classify(bitmap, rotationDegrees)
            onResults(results)
        }
        frameSkipCounter++
        image.close()
    }
}