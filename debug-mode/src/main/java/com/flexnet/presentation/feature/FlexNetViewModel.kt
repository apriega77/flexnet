package com.flexnet.presentation.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexnet.domain.model.NetworkRule
import com.flexnet.domain.repository.HttpInspectorRepository
import com.flexnet.presentation.feature.add.AddRuleArgs
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class FlexNetViewModel @Inject constructor(private val httpInspector: HttpInspectorRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(FlexNetState())
    val state: StateFlow<FlexNetState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<FlexNetEvent>()
    val event = _event.asSharedFlow()

    val effect = MutableSharedFlow<FlexNetEffect>()

    init {
        _event.onEach {
            mapEvent(it)
        }.launchIn(viewModelScope)
    }

    fun sendEvent(event: FlexNetEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun mapEvent(event: FlexNetEvent) {
        viewModelScope.launch {
            when (event) {
                is FlexNetEvent.Navigation -> {
                    effect.emit(FlexNetEffect.Navigation(event.flexNetNav))
                }

                is FlexNetEvent.ToolbarChange -> {
                    _state.update {
                        it.copy(toolbarChange = event.toolbarChange)
                    }
                }

                is FlexNetEvent.SetNetworkRule -> {
                    _state.update {
                        it.copy(networkRule = event.addRuleArgs)
                    }
                }

                is FlexNetEvent.RemoveHttpInspector -> {
                    if (event.id == null) {
                        httpInspector.deleteAllHttpInspector()
                    } else {
                        httpInspector.deleteHttpInspector(event.id)
                    }
                }

                is FlexNetEvent.SetNetworkRuleFromHttpInspector -> {
                    _state.update {
                        it.copy(
                            networkRule = AddRuleArgs.Add(
                                NetworkRule(
                                    id = 0,
                                    title = "",
                                    isActive = true,
                                    url = event.httpInspector.httpInspectorItem.url,
                                    method = event.httpInspector.httpInspectorItem.method,
                                    httpCode = event.httpInspector.httpInspectorItem.code.toIntOrZero(),
                                    responseBody = event.httpInspector.response.bodyString,
                                ),
                            ),
                        )
                    }
                }

                is FlexNetEvent.SetHttpInspector -> {
                    _state.update {
                        it.copy(httpInspector = event.httpInspector)
                    }
                }

                is FlexNetEvent.SetMainScreenNav -> {
                    _state.update {
                        it.copy(mainScreenNav = event.mainScreenNav)
                    }
                }

                FlexNetEvent.PopBack.FromDetailToMainScreen -> {
                    effect.emit(FlexNetEffect.PopBack(FlexNetNav.MAIN))
                }

                FlexNetEvent.PopBack.FromAddRulesToMockScreen -> {
                    _state.update {
                        it.copy(mainScreenNav = MainScreenNav.Mock)
                    }
                    effect.emit(FlexNetEffect.PopBack(FlexNetNav.MAIN))
                }
            }
        }
    }
}
