package com.flexnet.presentation.feature.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flexnet.R
import com.flexnet.domain.model.HttpInspector
import com.flexnet.presentation.feature.FlexNetActivity
import com.flexnet.presentation.feature.FlexNetEvent
import com.flexnet.presentation.feature.FlexNetNav
import com.flexnet.presentation.feature.shareText
import com.flexnet.presentation.feature.shareTextAsFile
import com.flexnet.presentation.feature.toCapitalizeEachWord
import com.flexnet.presentation.feature.toolbar.FlexNetToolbarState
import com.flexnet.presentation.foundation.FlexNetColorScheme
import com.flexnet.presentation.foundation.FlexNetTypography

@Composable
internal fun DetailMainScreen(httpInspector: HttpInspector, event: (FlexNetEvent) -> Unit) {
    val screens = listOf(DetailNav.Summary, DetailNav.Request, DetailNav.Response)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val context = LocalContext.current as FlexNetActivity
    LaunchedEffect(Unit) {
        event(
            FlexNetEvent.ToolbarChange {
                FlexNetToolbarState.Detail(title = "Http Inspector Details") {
                    Box {
                        IconButton(onClick = {
                            setExpanded(true)
                        }) {
                            Image(
                                painterResource(R.drawable.ic_more_menu),
                                null,
                            )
                        }
                    }
                }
            },
        )
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { setExpanded(false) },
        offset = DpOffset(screenWidth, (48).dp),
    ) {
        DropdownMenuItem(
            onClick = {
                event(FlexNetEvent.SetNetworkRuleFromHttpInspector(httpInspector))
                event(FlexNetEvent.Navigation(FlexNetNav.ADD))
                setExpanded(false)
            },
            text = { Text(text = "Mock Response") },
        )

        DropdownMenuItem(
            onClick = {
                shareTextAsFile(context, httpInspector.share)
                setExpanded(false)
            },
            text = { Text(text = "Share as File") },
        )

        DropdownMenuItem(
            onClick = {
                context.shareText(httpInspector.share)
                setExpanded(false)
            },
            text = { Text(text = "Share as Text") },
        )

        DropdownMenuItem(
            onClick = {
                event(FlexNetEvent.RemoveHttpInspector(httpInspector.id))
                event(FlexNetEvent.PopBack.FromDetailToMainScreen)
                setExpanded(false)
            },
            text = { Text(text = "Remove") },
        )
    }

    Scaffold(topBar = {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = FlexNetColorScheme.primary,
            indicator = {},
        ) {
            screens.forEachIndexed { index, screen ->
                val isSelected =
                    remember(selectedTabIndex) { selectedTabIndex == index }
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
                        .background(
                            if (isSelected) {
                                Color(0xFF2459C1)
                            } else {
                                Color(
                                    0xFF0C47BA,
                                )
                            },
                        ),
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        navController.navigate(screen.name) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = {
                        Text(
                            text = screen.name.lowercase(Locale.current.platformLocale)
                                .toCapitalizeEachWord(),
                            style = FlexNetTypography.titleLarge,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color(0xFF85A3DD),
                )
            }
        }
    }) {
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            startDestination = DetailNav.Summary.name,
        ) {
            composable(DetailNav.Summary.name) {
                SummaryScreen(httpInspector.httpInspectorSummary)
            }

            composable(DetailNav.Request.name) {
                RequestScreen(httpInspector.request)
            }

            composable(DetailNav.Response.name) {
                ResponseScreen(httpInspector.response)
            }
        }
    }
}
