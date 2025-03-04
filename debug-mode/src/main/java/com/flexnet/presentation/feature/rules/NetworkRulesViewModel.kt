package com.flexnet.presentation.feature.rules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexnet.domain.repository.NetworkRuleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class NetworkRulesViewModel @Inject constructor(
    private val repository: NetworkRuleRepository,
) :
    ViewModel() {
    private val _state = MutableStateFlow(NetworkRulesState())
    val state: StateFlow<NetworkRulesState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<NetworkRulesEvent>()

    val effect = MutableSharedFlow<NetworkRulesEffect>()

    init {
        _event.onEach {
            mapEvent(it)
        }.launchIn(viewModelScope)
    }

    fun sendEvent(event: NetworkRulesEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun mapEvent(event: NetworkRulesEvent) {
        viewModelScope.launch {
            when (event) {
                NetworkRulesEvent.GetNetworkRules -> {
                    val rule = repository.getRule()
                    if (rule.isNotEmpty()) {
                        _state.update {
                            it.copy(networkRules = rule)
                        }
                    }
                }

                is NetworkRulesEvent.NavigateToAddRule -> {
                    effect.emit(NetworkRulesEffect.NavigateToAddRule(event.addRuleArgs))
                }

                is NetworkRulesEvent.NetworkRuleToggle -> {
                    repository.saveRule(event.networkRule.copy(isActive = event.isActive))
                    sendEvent(NetworkRulesEvent.GetNetworkRules)
                }
            }
        }
    }
}
