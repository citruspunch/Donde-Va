package com.example.dondeva.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object ScanningPage

@Composable
fun AppRouteNavigator(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen {
                navController.navigate(ScanningPage)
            }
        }
        composable<ScanningPage> {
            ScanningPage{
                navController.navigate(Login)
            }
        }

    }
}