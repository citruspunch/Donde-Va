package com.example.dondeva.presentation.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

fun Timestamp.toLocaleFormat(): String =
    SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault()).format(this.toDate())