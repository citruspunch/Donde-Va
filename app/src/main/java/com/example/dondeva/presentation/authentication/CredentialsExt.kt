package com.example.dondeva.presentation.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dondeva.R

@Composable
fun AuthenticationButton(buttonText: Int, onClick: () -> Unit) {
    OutlinedButton (
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google_g),
            modifier = Modifier.padding(horizontal = 16.dp),
            contentDescription = stringResource(R.string.google_logo_description)
        )

        Text(
            text = stringResource(buttonText),
            fontSize = 15.sp,
            modifier = Modifier.padding(0.dp, 6.dp)
        )
    }
}
