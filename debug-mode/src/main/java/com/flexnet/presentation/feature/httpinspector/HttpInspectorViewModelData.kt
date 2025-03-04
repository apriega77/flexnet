package com.flexnet.presentation.feature.httpinspector

import com.flexnet.domain.model.HttpInspector

data class HttpInspectorState(
    val httpInspectorList: List<HttpInspector> = emptyList(),
    val searchState: String = "",
)

sealed interface HttpInspectorEvent {
    data class GetData(val search: String, val id: String? = null) : HttpInspectorEvent
    data class OnSearchChange(val args: String) : HttpInspectorEvent
    data class NavigateToDetail(val httpInspector: HttpInspector) : HttpInspectorEvent
}

sealed interface HttpInspectorEffect {
    data class NavigateToDetail(val httpInspector: HttpInspector) : HttpInspectorEffect
}
