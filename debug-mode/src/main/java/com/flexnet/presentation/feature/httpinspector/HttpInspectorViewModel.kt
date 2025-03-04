package com.flexnet.presentation.feature.httpinspector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexnet.domain.repository.HttpInspectorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class HttpInspectorViewModel @Inject constructor(
    private val repository: HttpInspectorRepository,
) :
    ViewModel() {

    private val _state = MutableStateFlow(HttpInspectorState())
    val state: StateFlow<HttpInspectorState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<HttpInspectorEvent>()

    val effect = MutableSharedFlow<HttpInspectorEffect>()

    init {
        _event.onEach {
            mapEvent(it)
        }.launchIn(viewModelScope)
    }

    fun sendEvent(event: HttpInspectorEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun mapEvent(event: HttpInspectorEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is HttpInspectorEvent.NavigateToDetail -> {
                    effect.emit(HttpInspectorEffect.NavigateToDetail(event.httpInspector))
                }

                is HttpInspectorEvent.GetData -> {
                    repository.getHttpInspector(event.search)
                        .collect { httpInspector ->
                            _state.update {
                                it.copy(httpInspectorList = httpInspector)
                            }

                            if (event.id != null) {
                                httpInspector.find {
                                    it.id == event.id
                                }?.let { HttpInspectorEffect.NavigateToDetail(it) }
                                    ?.let { effect.emit(it) }
                            }
                        }
                }

                is HttpInspectorEvent.OnSearchChange -> {
                    _state.update {
                        it.copy(searchState = event.args)
                    }
                    sendEvent(HttpInspectorEvent.GetData(event.args))
                }
            }
        }
    }
}
