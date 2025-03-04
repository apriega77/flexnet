package com.flexnet.presentation.feature.rules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flexnet.presentation.base.ViewModelFactoryProvider
import com.flexnet.presentation.feature.FlexNetActivity
import com.flexnet.presentation.feature.FlexNetEvent
import com.flexnet.presentation.feature.FlexNetNav

@Composable
internal fun NetworkRulesMainScreen(flexNetEvent: (FlexNetEvent) -> Unit) {
    val context = LocalContext.current as FlexNetActivity
    val viewModel: NetworkRulesViewModel =
        viewModel(factory = ViewModelFactoryProvider(context.viewModelComponent.getViewModelFactory()))
    val state = viewModel.state.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.sendEvent(NetworkRulesEvent.GetNetworkRules)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect {
            when (it) {
                is NetworkRulesEffect.NavigateToAddRule -> {
                    flexNetEvent.invoke(FlexNetEvent.SetNetworkRule(it.addRuleArgs))
                    flexNetEvent.invoke(FlexNetEvent.Navigation(FlexNetNav.ADD))
                }
            }
        }
    }

    NetworkRulesScreen(state = state.value) {
        viewModel.sendEvent(it)
    }
}
