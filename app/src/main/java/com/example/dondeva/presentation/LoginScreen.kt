package com.example.dondeva.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.dondeva.R
import com.example.dondeva.ui.theme.aqua

@Composable
fun LoginScreen(onNavigateToScanningPage: () -> Unit){
    LoginHeader()
    LoginBody(onNavigateToScanningPage)
}

@Composable
fun LoginHeader(){
    Row(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ){
        Text(
            text = "¿Dónde va?",
            color = aqua,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LoginBody(onNavigateToScanningPage: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Icon(
            painter = painterResource(R.drawable.baseline_account_circle_24),
            contentDescription = "icono",
            modifier = Modifier.height(100.dp).width(100.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "INICIO DE SESIÓN",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            leadingIcon = {Icon(imageVector = Icons.Default.Email, contentDescription = "email")},
            placeholder = {Text("Correo electrónico")},
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            leadingIcon = {Icon(imageVector = Icons.Default.Key, contentDescription = "key")},
            placeholder = {Text("Contraseña")},
            shape = RoundedCornerShape(25.dp),
            modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(25.dp)),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = { onNavigateToScanningPage() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = aqua,
                contentColor = Color.White
            ),

        ){
            Text(
                text = "Iniciar sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )


        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun previewLogin(){
//    LoginScreen()
//}
