package com.flexnet.domain.model

import androidx.compose.ui.graphics.Color

data class HttpInspectorItem(
    val stateInterceptor: HttpInspectorStateInterceptor,
    val code: String,
    val method: Method,
    val url: String,
    val timeStamp: String,
    val speed: String,
    val isMock: Boolean,
    val color: Color,
)
