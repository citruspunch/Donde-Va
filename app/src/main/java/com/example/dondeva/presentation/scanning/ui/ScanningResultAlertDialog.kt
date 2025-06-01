package com.example.dondeva.presentation.scanning.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dondeva.R
import com.example.dondeva.presentation.scanning.domain.GarbageType
import java.util.Timer
import kotlin.concurrent.schedule

@Composable
fun ScanningResultAlertDialog(
    garbageType: GarbageType,
    onDismissRequest: () -> Unit,
    onSeeDetails: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val animatedScale: Float by animateFloatAsState(if (isExpanded) 1f else 0f, label = "scale")

    LaunchedEffect(Unit) {
        Timer().schedule(delay = 250) { isExpanded = true }
    }

    AlertDialog(
        icon = {
            Box(modifier = Modifier.size(300.dp)) {
                Image(
                    painter = painterResource(garbageType.icon),
                    contentDescription = null,
                    modifier = Modifier.scale(animatedScale),
                )
            }
        },
        title = {
            Text(text = stringResource(garbageType.key))
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = onSeeDetails) {
                Text(text = stringResource(R.string.see_details))
            }
        },
    )
}