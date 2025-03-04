package com.flexnet.presentation.feature

import com.flexnet.domain.model.HttpInspector
import com.flexnet.presentation.feature.add.AddRuleArgs
import com.flexnet.presentation.feature.toolbar.FlexNetToolbarState

internal data class FlexNetState(
    val toolbarChange: () -> FlexNetToolbarState = { FlexNetToolbarState.MainScreen {} },
    val mainScreenNav: MainScreenNav = MainScreenNav.Inspector,
    val networkRule: AddRuleArgs = AddRuleArgs.Add(null),
    val httpInspector: HttpInspector? = null,
)

internal sealed interface FlexNetEvent {
    data class ToolbarChange(val toolbarChange: () -> FlexNetToolbarState) : FlexNetEvent
    data class Navigation(val flexNetNav: FlexNetNav) : FlexNetEvent
    sealed interface PopBack : FlexNetEvent {
        data object FromAddRulesToMockScreen : PopBack
        data object FromDetailToMainScreen : PopBack
    }
    data class SetMainScreenNav(val mainScreenNav: MainScreenNav) : FlexNetEvent
    data class SetNetworkRule(val addRuleArgs: AddRuleArgs) : FlexNetEvent
    data class SetNetworkRuleFromHttpInspector(val httpInspector: HttpInspector) : FlexNetEvent
    data class SetHttpInspector(val httpInspector: HttpInspector) : FlexNetEvent
    data class RemoveHttpInspector(val id: String? = null) : FlexNetEvent
}

internal sealed interface FlexNetEffect {
    data class Navigation(val flexNetNav: FlexNetNav) : FlexNetEffect
    data class PopBack(val flexNetNav: FlexNetNav) : FlexNetEffect
}

enum class MainScreenNav {
    Inspector, Mock
}
