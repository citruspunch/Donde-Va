package com.example.dondeva.domain

import android.graphics.Bitmap

interface GarbageClassifier {
    fun classify(bitmap: Bitmap, rotation: Int): List<Classification>
}