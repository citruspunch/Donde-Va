package com.example.dondeva.presentation.history

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.dondeva.domain.entity.getShortLabel
import com.example.dondeva.domain.service.AccountService
import com.example.dondeva.domain.service.StorageService
import com.example.dondeva.ui.theme.Purple40
import com.example.dondeva.ui.theme.PurpleGrey40
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HistoryScreen(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    accountService: AccountService,
    storageService: StorageService
) {
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(accountService, storageService)
    )

    LaunchedEffect(Unit) { viewModel.initialize(restartApp) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick(openScreen) },
                modifier = modifier.padding(16.dp),
                containerColor = Purple40,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) {
        val garbageItems by viewModel.garbageItems.collectAsState(emptyList())
        var showExitAppDialog by remember { mutableStateOf(false) }
        var showRemoveAccDialog by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { showExitAppDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir")
                    }
                    IconButton(onClick = { showRemoveAccDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.person_remove),
                            contentDescription = "Borrar cuenta",
                            tint = PurpleGrey40
                        )
                    }
                }
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                LazyColumn {
                    items(garbageItems, key = { it.id }) { garbageItem ->
                        GarbageElement(
                            garbageItem = garbageItem,
                            onActionClick = { viewModel.onItemClick(openScreen, garbageItem) }
                        )
                    }
                }
            }

            if (showExitAppDialog) {
                AlertDialog(
                    title = { Text(stringResource(R.string.sign_out_title)) },
                    text = { Text(stringResource(R.string.sign_out_description)) },
                    dismissButton = {
                        Button(onClick = { showExitAppDialog = false }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.onSignOutClick()
                            showExitAppDialog = false
                        }) {
                            Text(text = stringResource(R.string.sign_out))
                        }
                    },
                    onDismissRequest = { showExitAppDialog = false }
                )
            }

            if (showRemoveAccDialog) {
                AlertDialog(
                    title = { Text(stringResource(R.string.delete_account_title)) },
                    text = { Text(stringResource(R.string.delete_account_description)) },
                    dismissButton = {
                        Button(onClick = { showRemoveAccDialog = false }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.onDeleteAccountClick()
                            showRemoveAccDialog = false
                        }) {
                            Text(text = stringResource(R.string.delete_account))
                        }
                    },
                    onDismissRequest = { showRemoveAccDialog = false }
                )
            }
        }
    }
}

@Composable
fun GarbageElement(
    garbageItem: GarbageItem,
    onActionClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onActionClick(garbageItem.id) }
        ) {
            Text(
                text = garbageItem.getShortLabel(),
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
