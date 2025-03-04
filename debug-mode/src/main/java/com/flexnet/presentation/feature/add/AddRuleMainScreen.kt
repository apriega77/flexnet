package com.flexnet.presentation.feature.add

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flexnet.presentation.base.ViewModelFactoryProvider
import com.flexnet.presentation.feature.FlexNetActivity
import com.flexnet.presentation.feature.FlexNetEvent
import com.flexnet.presentation.feature.toolbar.FlexNetToolbarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun AddRuleMainScreen(addRuleArgs: AddRuleArgs, flexNetEvent: (FlexNetEvent) -> Unit) {
    val context = LocalContext.current as FlexNetActivity
    val viewModel: AddRuleViewModel =
        viewModel(factory = ViewModelFactoryProvider(context.viewModelComponent.getViewModelFactory()))
    val state = viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        flexNetEvent.invoke(
            FlexNetEvent.ToolbarChange {
                FlexNetToolbarState.AddRule
            },
        )
        viewModel.sendEvent(AddRuleEvent.SetArgs(addRuleArgs))
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect {
            when (it) {
                AddRuleEffect.PopBackToNetworkRulesScreen -> flexNetEvent.invoke(
                    FlexNetEvent.PopBack.FromAddRulesToMockScreen,
                )

                is AddRuleEffect.ShowSnackBar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(it.text)
                    }
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        AddRuleScreen(state = state.value) {
            viewModel.sendEvent(it)
        }
    }
}
