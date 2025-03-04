package com.flexnet.data.room.entity.httpinspector

import androidx.room.ColumnInfo

data class HttpResponseInspectorEntity(
    @ColumnInfo(name = "response_code")
    val responseCode: Int,
    @ColumnInfo(name = "response_message")
    val responseMessage: String,
    @ColumnInfo(name = "response_body")
    val responseBody: String?,
    @ColumnInfo(name = "response_body_content_type")
    val responseContentType: String?,
    @ColumnInfo(name = "response_handshake")
    val responseHandshake: String?,
    @ColumnInfo(name = "response_handshake_cipher_suite")
    val responseCipherSuite: String?,
    @ColumnInfo(name = "response_handshake_tls_version")
    val responseTlsVersion: String?,
    @ColumnInfo(name = "response_headers")
    val responseHeaders: String,
    @ColumnInfo(name = "response_received_at_millis")
    val responseReceivedAtMillis: Long,
    @ColumnInfo(name = "response_sent_at_millis")
    val responseSentAtMillis: Long,
    @ColumnInfo(name = "response_is_redirect")
    val responseIsRedirect: Boolean,
    @ColumnInfo(name = "response_is_successful")
    val responseIsSuccessful: Boolean,
    @ColumnInfo(name = "response_protocol")
    val responseProtocol: String,
    @ColumnInfo(name = "response_cache")
    val responseCache: String?,
    @ColumnInfo(name = "response_network")
    val responseNetwork: String?,
    @ColumnInfo(name = "response_prior_response")
    val responsePriorResponse: String?,
)
