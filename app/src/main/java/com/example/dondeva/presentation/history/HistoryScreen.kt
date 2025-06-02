@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.dondeva.presentation.history

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dondeva.R
import com.example.dondeva.domain.entity.GarbageItem
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.domain.service.StorageService
import com.example.dondeva.presentation.utils.toLocaleFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    accountService: AccountService,
    storageService: StorageService,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val viewModel: HistoryViewModel = viewModel {
        HistoryViewModel(accountService, storageService)
    }
    val garbageItems by viewModel.garbageItems.collectAsState(emptyList())
    var showExitAppDialog by remember { mutableStateOf(false) }
    var showRemoveAccDialog by remember { mutableStateOf(false) }
    var listState = rememberLazyListState()
    val extendFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex <= 1 }
    }

    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    when {
        showExitAppDialog -> AlertDialog(
            title = {
                Text(stringResource(R.string.sign_out_title))
            },
            text = {
                Text(stringResource(R.string.sign_out_description))
            },
            dismissButton = {
                TextButton(onClick = { showExitAppDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onSignOutClick()
                        showExitAppDialog = false
                    },
                ) {
                    Text(text = stringResource(R.string.sign_out))
                }
            },
            onDismissRequest = { showExitAppDialog = false },
        )

        showRemoveAccDialog -> AlertDialog(
            title = {
                Text(stringResource(R.string.delete_account_title))
            },
            text = {
                Text(stringResource(R.string.delete_account_description))
            },
            dismissButton = {
                Button(onClick = { showRemoveAccDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDeleteAccountClick()
                        showRemoveAccDialog = false
                    },
                ) {
                    Text(text = stringResource(R.string.delete_account))
                }
            },
            onDismissRequest = { showRemoveAccDialog = false },
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddClick(openScreen) }) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Add")
                    AnimatedVisibility(visible = extendFab) {
                        Text(
                            stringResource(R.string.analyze_another_object),
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.history))
                },
                actions = {
                    IconButton(onClick = { showRemoveAccDialog = true }) {
                        Icon(
                            Icons.Outlined.PersonRemove,
                            contentDescription = stringResource(R.string.delete_account),
                        )
                    }
                    IconButton(onClick = { showExitAppDialog = true }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 0.dp,
                ),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                garbageItems.sortedByDescending { it.scanningTime },
                key = { it.id },
            ) { garbageItem ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    GarbageCard(
                        garbageItem = garbageItem,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                    ) {
                        viewModel.onItemClick(openScreen, garbageItem)
                    }
                }
            }
        }
    }
}

@Composable
fun GarbageCard(
    garbageItem: GarbageItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onActionClick: (String) -> Unit,
) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onActionClick(garbageItem.id) },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(garbageItem.type.key),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "${stringResource(R.string.scanned)}: ${garbageItem.scanningTime.toLocaleFormat()}",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            with(sharedTransitionScope) {
                Image(
                    painter = painterResource(garbageItem.type.icon),
                    contentDescription = "${stringResource(garbageItem.type.key)} icon",
                    modifier = Modifier
                        .size(80.dp)
                        .sharedElement(
                            state = sharedTransitionScope.rememberSharedContentState(key = garbageItem.id),
                            animatedVisibilityScope = animatedContentScope,
                        ),
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun HistoryScreenPreview() = AppTheme {
//    val accountService = AccountServiceImpl()
//    val storage = StorageServiceImpl(accountService)
//
//    HistoryScreen(
//        restartApp = {},
//        openScreen = {},
//        accountService = accountService,
//        storageService = storage,
//    )
//}
//
//@Preview
//@Composable
//fun GarbageCardPreview() = AppTheme {
//    GarbageCard(
//        garbageItem = GarbageItem(
//            id = "my-key",
//            userId = "my-id",
//            type = GarbageType.GLASS,
//        ),
//    ) {}
//}
