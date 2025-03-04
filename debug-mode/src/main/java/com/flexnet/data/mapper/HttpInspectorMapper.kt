package com.flexnet.data.mapper

import androidx.compose.ui.graphics.Color
import com.flexnet.data.headersToKeyValue
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.data.room.entity.httpinspector.HttpRequestInspectorEntity
import com.flexnet.data.room.entity.httpinspector.HttpResponseInspectorEntity
import com.flexnet.data.toHttpInspectorState
import com.flexnet.data.toMethod
import com.flexnet.domain.model.HttpInspector
import com.flexnet.domain.model.HttpInspectorItem
import com.flexnet.domain.model.HttpInspectorRequest
import com.flexnet.domain.model.HttpInspectorResponse
import com.flexnet.domain.model.HttpInspectorStateInterceptor
import com.flexnet.domain.model.HttpInspectorSummary
import com.flexnet.domain.model.KeyValue
import com.flexnet.domain.model.Method
import com.flexnet.presentation.feature.component.jsonviewer.JsonParser
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

internal class HttpInspectorMapper @Inject constructor() {

    fun mapHttpInspectorList(list: List<HttpInspectorEntity>): List<HttpInspector> {
        return list.map {
            mapHttpInspector(it)
        }
    }

    private fun mapHttpInspector(data: HttpInspectorEntity): HttpInspector {
        val summary = mapSummary(
            state = data.state.toHttpInspectorState(),
            data.httpRequestInspectorEntity,
            data.httpResponseInspectorEntity,
            data.error,
            createdAt = data.createdAt,
        )
        val request = mapRequest(data.httpRequestInspectorEntity)
        val response = mapResponse(data.httpResponseInspectorEntity)
        return HttpInspector(
            id = data.id.toString(),
            httpInspectorItem = HttpInspectorItem(
                stateInterceptor = data.state.toHttpInspectorState(),
                code = data.httpResponseInspectorEntity?.responseCode.toString(),
                method = data.httpRequestInspectorEntity?.requestMethod?.toMethod() ?: Method.ANY,
                url = data.httpRequestInspectorEntity?.requestUrl.orEmpty(),
                timeStamp = (
                    data.httpResponseInspectorEntity?.responseReceivedAtMillis
                        ?: "Loading"
                    ).toString(),
                speed = (
                    data.httpResponseInspectorEntity?.responseSentAtMillis
                        ?: "Loading"
                    ).toString(),
                isMock = data.isMocked ?: false,
                color = mapColor(
                    data.state.toHttpInspectorState(),
                    data.httpResponseInspectorEntity?.responseCode,
                ),
            ),
            httpInspectorSummary = summary,
            request = request,
            response = response,
            share = mapShare(
                mapSummary = summary,
                mapRequest = request,
                mapResponse = response,
            ),
        )
    }

    private fun mapShare(
        mapSummary: HttpInspectorSummary,
        mapRequest: HttpInspectorRequest,
        mapResponse: HttpInspectorResponse,
    ): String {
        return buildString {
            appendLine("--- Logged by FlexNet ---")
            appendLine()
            appendLine("--- SUMMARY ---")
            appendLine(mapSummary.toFormattedString())
            appendLine()
            appendLine("--- REQUEST ---")
            mapRequest.header.takeIf { it.isNotEmpty() }?.let { appendLine(it) }
            appendLine(mapRequest.bodyString.takeIf { it.isNotEmpty() } ?: "Failed To Load")
            appendLine()
            appendLine("--- RESPONSE ---")
            mapResponse.header.takeIf { it.isNotEmpty() }?.let { appendLine(it) }
            appendLine(mapResponse.bodyString.takeIf { it.isNotEmpty() } ?: "Failed To Load")
            appendLine()
            appendLine("--- Logged by FlexNet ---")
        }.trimEnd()
    }

    private fun HttpInspectorSummary.toFormattedString(): String {
        return summary.joinToString(separator = "\n") { keyValueList ->
            keyValueList.joinToString(separator = "\n") { keyValue ->
                "${keyValue.key}: ${keyValue.value}"
            }
        }
    }

    private fun mapSummary(
        state: HttpInspectorStateInterceptor,
        httpRequestInspectorEntity: HttpRequestInspectorEntity?,
        httpResponseInspectorEntity: HttpResponseInspectorEntity?,
        error: String?,
        createdAt: String?,
    ): HttpInspectorSummary {
        return HttpInspectorSummary(
            summary = listOf(
                listOf(
                    // Section 1
                    KeyValue(
                        key = "URL",
                        value = httpRequestInspectorEntity?.requestUrl.orEmpty(),
                    ),
                    KeyValue(
                        key = "Method",
                        value = httpRequestInspectorEntity?.requestMethod.orEmpty(),
                    ),
                    KeyValue(
                        key = "Protocol",
                        value = httpResponseInspectorEntity?.responseProtocol.orEmpty(),
                    ),
                    KeyValue(
                        key = "Status",
                        value = state.name,
                    ),
                    KeyValue(
                        key = "Response Code",
                        value = httpResponseInspectorEntity?.responseCode.toString(),
                    ),
                ),

                // Section 2
                listOf(
                    KeyValue(
                        key = "Https",
                        value = httpRequestInspectorEntity?.requestIsHttps.toString(),
                    ),
                    KeyValue(
                        key = "TLS version",
                        value = httpResponseInspectorEntity?.responseTlsVersion.orEmpty(),
                    ),
                    KeyValue(
                        key = "Cipher",
                        value = httpResponseInspectorEntity?.responseCipherSuite.orEmpty(),
                    ),
                ),

                // section 3
                listOf(
                    KeyValue(key = "Request Time", value = createdAt.orEmpty()),
                    KeyValue(
                        key = "Response Time",
                        value = httpResponseInspectorEntity?.responseReceivedAtMillis.toString(),
                    ),
                    KeyValue(
                        key = "Total Time",
                        value = (
                            createdAt?.toLongOrDefault(0L)
                                ?.let {
                                    httpResponseInspectorEntity?.responseReceivedAtMillis?.minus(
                                        it,
                                    )
                                }
                            ).toString(),
                    ),
                    KeyValue(
                        key = "Response Size",
                        value = (
                            httpResponseInspectorEntity?.responseBody?.length?.toLong()
                                ?: 0L
                            ).toString(),
                    ),
                    KeyValue(
                        key = "Request Size",
                        value = (
                            httpRequestInspectorEntity?.requestContentLength
                                ?: 0L
                            ).toString(),
                    ),
                    KeyValue(
                        key = "Total Size",
                        value = (
                            (
                                httpResponseInspectorEntity?.responseBody?.length?.toLong()
                                    ?: 0L
                                ) + (
                                httpRequestInspectorEntity?.requestContentLength
                                    ?: 0L
                                )
                            ).toString(),
                    ),
                ),

            ),
        )
    }

    private fun mapResponse(httpResponseInspectorEntity: HttpResponseInspectorEntity?): HttpInspectorResponse {
        val body = httpResponseInspectorEntity?.responseBody.toString()
        val json = JsonParser().safeParse(body)
        return HttpInspectorResponse(
            header = httpResponseInspectorEntity?.responseHeaders?.headersToKeyValue()
                ?: emptyList(),
            bodyJson = json,
            bodyString = body,
        )
    }

    private fun mapRequest(httpRequestInspectorEntity: HttpRequestInspectorEntity?): HttpInspectorRequest {
        val body = httpRequestInspectorEntity?.requestBody.toString()
        val json = JsonParser().safeParse(body)
        return HttpInspectorRequest(
            header = httpRequestInspectorEntity?.requestHeaders?.headersToKeyValue() ?: emptyList(),
            bodyJson = json,
            bodyString = body,
        )
    }

    private fun mapColor(state: HttpInspectorStateInterceptor, responseCode: Int?): Color {
        return when (state) {
            HttpInspectorStateInterceptor.REQUEST -> Color(0xFF3399FF)
            HttpInspectorStateInterceptor.SUCCESS -> {
                when (responseCode) {
                    in 200 until 299 -> Color(0xFF3CCD50)
                    in 300 until 399 -> Color(0xFF2D4CEF)
                    in 400 until 499 -> Color(0xFFEF8B2D)
                    in 500..599 -> Color(0xFFE63D32)
                    else -> Color(0xFF3399FF)
                }
            }

            HttpInspectorStateInterceptor.FAILED -> Color(0xFFE63D32)
        }
    }
}
