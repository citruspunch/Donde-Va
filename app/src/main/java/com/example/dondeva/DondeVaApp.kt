package com.example.dondeva

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dondeva.data.impl.AccountServiceImpl
import com.example.dondeva.presentation.sign_up.SignUpScreen
import com.example.dondeva.presentation.sing_in.SignInScreen
import com.example.dondeva.presentation.splash.SplashScreen
import com.example.dondeva.ui.theme.DondeVaTheme

@Composable
fun DondeVaApp() {
    DondeVaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    appGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        AppState(navController)
    }

fun NavGraphBuilder.appGraph(appState: AppState) {
    composable(SCAN_SCREEN) {
        // TODO implement scanning screen
        /*NotesListScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )*/
    }

    // Por si hacemos vista de un solo articulo del historial
    composable(
        route = "$HISTORY_SCREEN$ITEM_ID_ARG",
        arguments = listOf(navArgument(ITEM_ID) { defaultValue = ITEM_DEFAULT_ID })
    ) {
        // TODO implement note screen
    }

    composable(HISTORY_SCREEN) {
        // TODO implement history screen
        /*HistoryScreen(
            openScreen = { route -> appState.navigate(route) }
        )*/
    }

    composable(SIGN_IN_SCREEN) {
        SignInScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = AccountServiceImpl()
        )
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = AccountServiceImpl()
        )
    }

    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            accountService = AccountServiceImpl()
        )
    }
}