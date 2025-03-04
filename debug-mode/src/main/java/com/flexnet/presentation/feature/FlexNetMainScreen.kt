package com.flexnet.presentation.feature

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.flexnet.R
import com.flexnet.presentation.base.ViewModelFactoryProvider
import com.flexnet.presentation.feature.add.AddRuleMainScreen
import com.flexnet.presentation.feature.component.navigation.navigationComponent
import com.flexnet.presentation.feature.detail.DetailMainScreen
import com.flexnet.presentation.feature.httpinspector.HttpInspectorMainScreen
import com.flexnet.presentation.feature.rules.NetworkRulesMainScreen
import com.flexnet.presentation.feature.toolbar.FlexNetToolbarState
import com.flexnet.presentation.foundation.FlexNetColorScheme
import com.flexnet.presentation.foundation.FlexNetTypography
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun FlexNetMainScreen() {
    val context = LocalContext.current as FlexNetActivity
    val viewModel: FlexNetViewModel =
        viewModel(factory = ViewModelFactoryProvider(context.viewModelComponent.getViewModelFactory()))
    val navController = rememberNavController()
    val state by viewModel.state.collectAsState()
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(color = FlexNetColorScheme.primary)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FlexNetEffect.Navigation -> navController.navigate(effect.flexNetNav.route)
                is FlexNetEffect.PopBack -> navController.popBackStack(
                    effect.flexNetNav.route,
                    false,
                )
            }
        }
    }

    val paddingStart by remember(state) { mutableStateOf(if (state.toolbarChange.invoke().hasBackButton) 0.dp else 16.dp) }
    Scaffold(
        topBar = {
            val onBackPressedDispatcher =
                LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FlexNetColorScheme.primary)
                    .padding(start = paddingStart, end = 16.dp, top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (state.toolbarChange.invoke().hasBackButton) {
                    IconButton(
                        onClick = { onBackPressedDispatcher?.onBackPressed() },
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "back",
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }

                Text(
                    text = state.toolbarChange.invoke().title,
                    style = FlexNetTypography.headlineSmall.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                )

                state.toolbarChange.invoke().trailingIcon?.let { trailingIcon ->
                    Box(modifier = Modifier) { trailingIcon.invoke() }
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = FlexNetNav.MAIN.route,
        ) {
            navigationComponent(FlexNetNav.MAIN.route) {
                MainScreen(state.mainScreenNav) {
                    viewModel.sendEvent(it)
                }
            }

            navigationComponent(FlexNetNav.DETAIL.route) {
                state.httpInspector?.let {
                    DetailMainScreen(it) {
                        viewModel.sendEvent(it)
                    }
                }
            }

            navigationComponent(FlexNetNav.ADD.route) {
                AddRuleMainScreen(state.networkRule) { viewModel.sendEvent(it) }
            }
        }
    }
}

@Composable
internal fun MainScreen(nav: MainScreenNav, event: (FlexNetEvent) -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(if (nav == MainScreenNav.Inspector) 0 else 1) }

    LaunchedEffect(Unit) {
        event(
            FlexNetEvent.ToolbarChange {
                FlexNetToolbarState.MainScreen {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {}) { ToolbarIcon(R.drawable.ic_picture_in_picture) }
                        IconButton(onClick = {
                            if (selectedTabIndex == 0) {
                                event(FlexNetEvent.RemoveHttpInspector())
                            }
                        }) { ToolbarIcon(R.drawable.ic_delete) }
                    }
                }
            },
        )
    }

    val tabs = listOf("Inspector", "Mock")

    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = FlexNetColorScheme.primary,
            indicator = {},
        ) {
            tabs.forEachIndexed { index, screen ->
                val isSelected = selectedTabIndex == index
                Tab(
                    modifier = Modifier
                        .clip(
                            if (isSelected) {
                                RoundedCornerShape(
                                    topEnd = 8.dp,
                                    topStart = 8.dp,
                                )
                            } else {
                                RoundedCornerShape(0.dp)
                            },
                        )
                        .background(if (isSelected) Color(0xFF2459C1) else FlexNetColorScheme.primary),
                    selected = isSelected,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = tabs[index],
                            style = FlexNetTypography.titleLarge,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color(0xFF85A3DD),
                )
            }
        }

        when (selectedTabIndex) {
            0 -> HttpInspectorMainScreen {
                event(FlexNetEvent.SetMainScreenNav(MainScreenNav.Inspector))
                event(it)
            }

            1 -> NetworkRulesMainScreen {
                event(FlexNetEvent.SetMainScreenNav(MainScreenNav.Mock))
                event(it)
            }
        }
    }
}

@Composable
private fun ToolbarIcon(iconResId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(iconResId),
        contentDescription = "",
        modifier = modifier.size(32.dp),
    )
}
