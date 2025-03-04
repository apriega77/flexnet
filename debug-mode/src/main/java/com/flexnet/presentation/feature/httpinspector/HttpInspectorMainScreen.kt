package com.flexnet.presentation.feature.httpinspector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flexnet.presentation.base.ViewModelFactoryProvider
import com.flexnet.presentation.feature.FlexNetActivity
import com.flexnet.presentation.feature.FlexNetEvent
import com.flexnet.presentation.feature.FlexNetNav
import com.flexnet.presentation.feature.component.NetworkItem
import com.flexnet.presentation.feature.component.empty.EmptyScreen
import com.flexnet.presentation.foundation.FlexNetTheme

@Composable
internal fun HttpInspectorMainScreen(
    flexNetEvent: (FlexNetEvent) -> Unit,
) {
    val context = LocalContext.current as FlexNetActivity
    val viewModel: HttpInspectorViewModel =
        viewModel(factory = ViewModelFactoryProvider(context.viewModelComponent.getViewModelFactory()))
    val state = viewModel.state.collectAsState()
    val httpInspectorId = context.httpInspectorNotificationArgs

    LaunchedEffect(Unit) {
        viewModel.sendEvent(HttpInspectorEvent.GetData("", httpInspectorId?.id))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is HttpInspectorEffect.NavigateToDetail -> {
                    flexNetEvent(FlexNetEvent.SetHttpInspector(it.httpInspector))
                    flexNetEvent(FlexNetEvent.Navigation(FlexNetNav.DETAIL))
                }
            }
        }
    }

    FlexNetTheme {
        HttpInspectorScreen(state.value) {
            viewModel.sendEvent(it)
        }
    }
}

@Composable
private fun HttpInspectorScreen(state: HttpInspectorState, event: (HttpInspectorEvent) -> Unit) {
    val showSearch by remember { mutableStateOf(state.httpInspectorList.size > 10) }
    Scaffold(topBar = {
        if (showSearch) {
            OutlinedTextField(
                value = state.searchState,
                onValueChange = {
                    event(HttpInspectorEvent.OnSearchChange(it))
                },
                label = { Text(text = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
            )
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(color = Color(0xFFF7F9FD)),
        ) {
            itemsIndexed(state.httpInspectorList) { index, item ->
                val paddingBottom = remember {
                    if (index == state.httpInspectorList.lastIndex) 24.dp else 0.dp
                }
                NetworkItem(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = paddingBottom)
                        .clickable {
                            event(HttpInspectorEvent.NavigateToDetail(item))
                        },
                    item.httpInspectorItem,
                )
            }
        }

        val showEmptyScreen by remember(state) { mutableStateOf(state.httpInspectorList.isEmpty()) }
        if (showEmptyScreen) {
            EmptyScreen()
        }
    }
}
