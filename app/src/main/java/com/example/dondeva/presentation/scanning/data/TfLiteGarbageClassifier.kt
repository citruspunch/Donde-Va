package com.example.dondeva.presentation.scanning.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.example.dondeva.presentation.scanning.domain.Classification
import com.example.dondeva.presentation.scanning.domain.GarbageClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier


class TfLiteGarbageClassifier(
    private val context: Context,
    private val treshold: Float = 0.5f,
    private val maxResults: Int = 1,
) : GarbageClassifier {
    private var classifier: ImageClassifier? = null
    private var labels = arrayListOf("cardboard", "glass", "metal", "paper", "plastic", "trash")

    private fun setupClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(treshold)
            .build()

        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "garbage_classifier.tflite",
                options
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation =
        when (rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }

    override fun classify(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null) {
            setupClassifier()
        }

        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()

        val result = classifier!!.classify(tensorImage, imageProcessingOptions)

        return result
            .flatMap { classification ->
                classification.categories.map { category ->
                    Classification(
                        name = labels[category.index],
                        score = category.score,
                    )
                }
            }.distinctBy { it.name }
    }
}