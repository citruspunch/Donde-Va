package com.example.dondeva.presentation.scanning.domain

import android.graphics.Bitmap

interface GarbageClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}