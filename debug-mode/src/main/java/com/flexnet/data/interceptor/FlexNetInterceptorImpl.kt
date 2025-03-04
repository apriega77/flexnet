package com.flexnet.data.interceptor

import com.flexnet.api.FlexNetInterceptor
import com.flexnet.api.FlexNetProperties
import com.flexnet.data.mapper.HttpInspectorEntityMapper
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.domain.model.HttpInspectorStateInterceptor
import com.flexnet.domain.model.NetworkRule
import com.flexnet.domain.repository.HttpInspectorRepository
import com.flexnet.domain.repository.NetworkRuleRepository
import com.flexnet.presentation.feature.notification.HttpInspectorNotificationHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import javax.inject.Inject

internal class FlexNetInterceptorImpl @Inject constructor(
    private val networkRuleRepository: NetworkRuleRepository,
    private val httpInspectorRepository: HttpInspectorRepository,
    private val flexNetProperties: FlexNetProperties,
    private val httpInspectorNotificationHelper: HttpInspectorNotificationHelper,
    private val httpInspectorEntityMapper: HttpInspectorEntityMapper,
) :
    FlexNetInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val notificationId = System.currentTimeMillis().toInt()
        val httpInspectorEntity = httpInspectorEntityMapper.mapHttpInspectorEntity(request)

        sendNotification(
            id = notificationId,
            httpInspectorEntity,
        )

        runBlocking {
            httpInspectorRepository.saveHttpInspector(httpInspectorEntity)
        }

        try {
            // Proceed with the original request to get the original response
            val originalResponse = chain.proceed(request)
            val url = request.url.toString()
            val method = request.method

            // Check for any matching rules
            val rules = runBlocking { networkRuleRepository.getRule() }
            val matchedRule = getMatchingRule(rules, url, method)

            // If there's a matching rule, create the modified response
            val modifiedResponse = matchedRule?.let {
                originalResponse.close()
                originalResponse.newBuilder()
                    .code(it.httpCode)
                    .body(it.responseBody.toResponseBody("application/json".toMediaType()))
                    .build()
            }

            val finalResponse = modifiedResponse ?: originalResponse

            // Prepare the HttpInspectorEntity for the response
            val responseEntity =
                httpInspectorEntityMapper.mapResponseEntity(httpInspectorEntity, finalResponse)

            // Send notification and save the final response (modified or original)
            sendNotification(notificationId, responseEntity)

            runBlocking {
                httpInspectorRepository.saveHttpInspector(responseEntity)
            }

            return finalResponse
        } catch (e: IOException) {
            val errorHttpInspectorEntity = httpInspectorEntity.copy(
                state = HttpInspectorStateInterceptor.FAILED.name,
                error = e.stackTraceToString(),
            )
            runBlocking {
                runBlocking {
                    httpInspectorRepository.saveHttpInspector(
                        errorHttpInspectorEntity,
                    )
                }
            }

            sendNotification(
                id = notificationId,
                errorHttpInspectorEntity,
            )
            throw e
        }
    }

    private fun getMatchingRule(
        list: List<NetworkRule>,
        url: String,
        method: String,
    ): NetworkRule? {
        return list.find {
            url.contains(it.url) && method.contains(it.method.name) && it.isActive && it.responseBody.isNotBlank()
        }
    }

    private fun sendNotification(
        id: Int,
        httpInspectorEntity: HttpInspectorEntity,
    ) {
        if (flexNetProperties.showNotification) {
            httpInspectorNotificationHelper.showNotification(
                id,
                httpInspectorEntity,
            )
        }
    }
}
