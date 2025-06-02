@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.example.dondeva.presentation.history

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dondeva.R
import com.example.dondeva.data.impl.AccountServiceImpl
import com.example.dondeva.data.impl.StorageServiceImpl
import com.example.dondeva.presentation.scanning.domain.GarbageType
import com.example.dondeva.presentation.utils.MarkdownViewer
import com.example.dondeva.presentation.utils.toLocaleFormat

@Composable
fun HistoryItemView(
    garbageItemId: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val historyViewModel = viewModel {
        val accountService = AccountServiceImpl()
        val storageService = StorageServiceImpl(accountService)
        HistoryViewModel(accountService = accountService, storageService = storageService)
    }
    val garbageItems by historyViewModel.garbageItems.collectAsState(emptyList())
    val filteredGarbageItem = remember(garbageItems, garbageItemId) {
        garbageItems.find { it.id == garbageItemId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            filteredGarbageItem?.let { garbageItem ->
                val (id, _, garbageType, scanningTime) = filteredGarbageItem
                with(sharedTransitionScope) {
                    Image(
                        painter = painterResource(garbageType.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(320.dp)
                            .sharedElement(
                                state = sharedTransitionScope.rememberSharedContentState(key = id),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                    )
                }
                Text(
                    stringResource(garbageType.key),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    "${stringResource(R.string.scanned)}: ${scanningTime.toLocaleFormat()}",
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = painterResource(R.drawable.recyclable),
                    contentDescription = "Recyclable icon",
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(8.dp))
                MarkdownViewer(
                    text = stringResource(
                        when (garbageType) {
                            GarbageType.CARDBOARD -> R.string.cardboard_information
                            GarbageType.GLASS -> R.string.glass_information
                            GarbageType.METAL -> R.string.metal_information
                            GarbageType.PAPER -> R.string.paper_information
                            GarbageType.PLASTIC -> R.string.plastic_information
                            GarbageType.MISCELLANEOUS -> R.string.miscellaneous_information
                        },
                    ),
                )
                Spacer(Modifier.height(16.dp))
                MarkdownViewer(text = stringResource(R.string.more_information_about_recyclables))
                Spacer(Modifier.height(16.dp))
            } ?: run {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text("${stringResource(R.string.loading_item)}...")
            }
        }
    }
}

//@Preview
//@Composable
//fun HistoryItemViewPreview() = AppTheme {
//    HistoryItemView(garbageItemId = "")
//}