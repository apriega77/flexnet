package com.flexnet.data.mapper

import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.data.room.entity.httpinspector.HttpRequestInspectorEntity
import com.flexnet.data.room.entity.httpinspector.HttpResponseInspectorEntity
import com.flexnet.domain.model.HttpInspectorStateInterceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import javax.inject.Inject

class HttpInspectorEntityMapper @Inject constructor() {

    fun mapHttpInspectorEntity(request: Request) = HttpInspectorEntity(
        state = HttpInspectorStateInterceptor.REQUEST.name,
        httpRequestInspectorEntity = HttpRequestInspectorEntity(
            requestMethod = request.method,
            requestUrl = request.url.toUrl().toString(),
            requestHost = request.url.host,
            requestScheme = request.url.scheme,
            requestIsHttps = request.isHttps,
            requestEncodedFragment = request.url.encodedFragment,
            requestEncodedPassword = request.url.encodedPassword,
            requestEncodedPath = request.url.encodedPath,
            requestEncodedPathSegments = request.url.encodedPathSegments,
            requestEncodedQuery = request.url.encodedQuery,
            requestEncodedUserName = request.url.encodedUsername,
            requestFragment = request.url.fragment,
            requestPassword = request.url.password,
            requestPathSegments = request.url.pathSegments,
            requestPathSize = request.url.pathSize,
            requestQuery = request.url.query,
            requestQueryParameterName = "",
            requestQuerySize = request.url.querySize,
            requestPort = request.url.port,
            requestUsername = request.url.username,
            requestBody = requestBodyToString(request),
            requestContentLength = request.body?.contentLength(),
            requestContentType = request.body?.contentType()?.type,
            requestHeaders = request.headers.toString(),
        ),
        createdAt = System.currentTimeMillis().toString(),
    )

    fun mapResponseEntity(
        httpInspectorEntity: HttpInspectorEntity,
        finalResponse: Response,
    ) = httpInspectorEntity.copy(
        state = HttpInspectorStateInterceptor.SUCCESS.name,
        httpResponseInspectorEntity = HttpResponseInspectorEntity(
            responseCode = finalResponse.code,
            responseMessage = finalResponse.message,
            responseBody = responseBodyToString(finalResponse),
            responseContentType = finalResponse.body?.contentType()?.type,
            responseHandshake = finalResponse.handshake.toString(),
            responseCipherSuite = finalResponse.handshake?.cipherSuite?.javaName,
            responseTlsVersion = finalResponse.handshake?.tlsVersion.toString(),
            responseHeaders = finalResponse.headers.toString(),
            responseReceivedAtMillis = finalResponse.receivedResponseAtMillis,
            responseSentAtMillis = finalResponse.sentRequestAtMillis,
            responseIsRedirect = finalResponse.isRedirect,
            responseIsSuccessful = finalResponse.isSuccessful,
            responseProtocol = finalResponse.protocol.name,
            responseCache = finalResponse.cacheResponse.toString(),
            responseNetwork = finalResponse.networkResponse.toString(),
            responsePriorResponse = finalResponse.priorResponse.toString(),
        ),
    )

    private fun requestBodyToString(request: Request): String {
        return request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } ?: "No Request Body"
    }

    private fun responseBodyToString(response: Response): String {
        val responseBody = response.body ?: return "No Response Body"
        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body
        val buffer = source.buffer

        return buffer.clone().readUtf8() // Read the buffered body as a string
    }
}
