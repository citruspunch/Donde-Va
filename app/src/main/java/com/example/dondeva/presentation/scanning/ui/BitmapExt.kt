package com.example.dondeva.presentation.scanning.ui

import android.graphics.Bitmap

fun Bitmap.centerCrop(desiredWidth: Int, desiredHeight: Int): Bitmap {
    val xStart = (width - desiredWidth) / 2
    val yStart = (height - desiredHeight) / 2

    if (xStart <  0 || yStart < 0 || desiredHeight > height || desiredWidth > width){
        throw IllegalArgumentException("Invalid arguments for center cropping")
    }
    return Bitmap.createBitmap(this, xStart, yStart, desiredWidth, desiredHeight)

}