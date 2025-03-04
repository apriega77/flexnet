package com.flexnet.domain.model

import com.flexnet.presentation.feature.component.jsonviewer.JsonItem

data class HttpInspectorResponse(
    val header: List<KeyValue>,
    val bodyJson: List<JsonItem>,
    val bodyString: String,
)
