@file:OptIn(ExperimentalTextApi::class)

package com.example.dondeva.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.dondeva.R

val bodyFontFamily = FontFamily(
    Font(
        R.font.manropevariable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
        ),
    ),
)

val displayFontFamily = FontFamily(
    Font(
        R.font.manropevariable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600),
            FontVariation.width(24f),
        ),
    ),
)

// Default Material 3 typography values
val baseline = Typography()

val letterSpacing = TextUnit(-1f, TextUnitType.Sp)

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily,
        letterSpacing = letterSpacing,
    ),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

