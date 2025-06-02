@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.dondeva

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dondeva.data.impl.AccountServiceImpl
import com.example.dondeva.data.impl.StorageServiceImpl
import com.example.dondeva.presentation.history.HistoryItemView
import com.example.dondeva.presentation.history.HistoryScreen
import com.example.dondeva.presentation.scanning.ui.ScanningPage
import com.example.dondeva.presentation.authentication.sign_up.SignUpScreen
import com.example.dondeva.presentation.authentication.sing_in.SignInScreen
import com.example.dondeva.presentation.splash.SplashScreen
import com.example.dondeva.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun DondeVaApp() {
    AppTheme {
        val appState = rememberAppState()

        SharedTransitionLayout {
            NavHost(
                navController = appState.navController,
                startDestination = SPLASH_SCREEN,
            ) {
                composable(route = SCAN_SCREEN) {
                    val scope = rememberCoroutineScope()

                    ScanningPage(
                        onNavigateToLoginPage = {
                            scope.launch {
                                AccountServiceImpl().signOut()
                                appState.navigateAndPopUp(
                                    route = SIGN_IN_SCREEN,
                                    popUp = SCAN_SCREEN,
                                )
                            }
                        },
                        onNavigateToHistoryView = { appState.navigate(route = HISTORY_SCREEN) },
                        onNavigateToHistoryItemView = { itemId ->
                            appState.navigate(route = "$RESULT_SCREEN?$ITEM_ID=$itemId")
                        },
                    )
                }

                composable(route = HISTORY_SCREEN) {
                    val accountService = AccountServiceImpl()
                    val storageService = StorageServiceImpl(accountService)

                    HistoryScreen(
                        restartApp = { route -> appState.clearAndNavigate(route) },
                        openScreen = { route -> appState.navigate(route) },
                        accountService = accountService,
                        storageService = storageService,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable,
                    )
                }

                composable(
                    route = "$RESULT_SCREEN?$ITEM_ID={$ITEM_ID}",
                    arguments = listOf(
                        navArgument(name = ITEM_ID) {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        },
                    ),
                ) { state ->
                    HistoryItemView(
                        garbageItemId = state.arguments!!.getString(ITEM_ID)!!,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable,
                    )
                }


                composable(SIGN_IN_SCREEN) {
                    SignInScreen(
                        openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                        accountService = AccountServiceImpl(),
                    )
                }

                composable(SIGN_UP_SCREEN) {
                    SignUpScreen(
                        openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                        onSignInRequired = {
                            appState.navigateAndPopUp(
                                route = SIGN_IN_SCREEN,
                                popUp = SIGN_UP_SCREEN,
                            )
                        },
                        accountService = AccountServiceImpl(),
                    )
                }

                composable(SPLASH_SCREEN) {
                    SplashScreen(
                        openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                        accountService = AccountServiceImpl(),
                    )
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

