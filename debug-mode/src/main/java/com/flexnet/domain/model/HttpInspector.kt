package com.flexnet.domain.model

data class HttpInspector(
    val id: String,
    val httpInspectorItem: HttpInspectorItem,
    val httpInspectorSummary: HttpInspectorSummary,
    val request: HttpInspectorRequest,
    val response: HttpInspectorResponse,
    val share: String,
)
