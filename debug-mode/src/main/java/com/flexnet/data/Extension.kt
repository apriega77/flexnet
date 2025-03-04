package com.flexnet.data

import com.flexnet.domain.model.HttpInspectorStateInterceptor
import com.flexnet.domain.model.KeyValue
import com.flexnet.domain.model.Method

fun String.toMethod(): Method {
    return Method.entries.firstOrNull {
        it.name.contains(this)
    } ?: Method.GET
}

fun String.toHttpInspectorState(): HttpInspectorStateInterceptor {
    return HttpInspectorStateInterceptor.entries.firstOrNull {
        it.name.contains(this)
    } ?: HttpInspectorStateInterceptor.REQUEST
}

fun String.headersToKeyValue(): List<KeyValue> {
    return try {
        this.trim('{', '}')
            .split(",")
            .map { s ->
                val (key, value) = s.split("=").map { it.trim() }
                KeyValue(key, value)
            }
            .filter { it.key.isNotEmpty() }
    } catch (e: Exception) {
        emptyList()
    }
}
