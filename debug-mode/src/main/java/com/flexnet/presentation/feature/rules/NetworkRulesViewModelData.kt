package com.flexnet.presentation.feature.rules

import com.flexnet.domain.model.NetworkRule
import com.flexnet.presentation.feature.add.AddRuleArgs

internal data class NetworkRulesState(val networkRules: List<NetworkRule> = emptyList())

internal sealed interface NetworkRulesEvent {
    object GetNetworkRules : NetworkRulesEvent
    data class NetworkRuleToggle(val isActive: Boolean, val networkRule: NetworkRule) :
        NetworkRulesEvent

    data class NavigateToAddRule(val addRuleArgs: AddRuleArgs) : NetworkRulesEvent
}

internal sealed interface NetworkRulesEffect {
    data class NavigateToAddRule(val addRuleArgs: AddRuleArgs) : NetworkRulesEffect
}
