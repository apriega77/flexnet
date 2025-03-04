package com.flexnet.data.room.entity.httpinspector

import androidx.room.ColumnInfo

data class HttpRequestInspectorEntity(
    @ColumnInfo(name = "request_method") val requestMethod: String?,
    @ColumnInfo(name = "request_url") val requestUrl: String?,
    @ColumnInfo(name = "request_url_host") val requestHost: String?,
    @ColumnInfo(name = "request_url_scheme") val requestScheme: String?,
    @ColumnInfo(name = "request_url_is_https") val requestIsHttps: Boolean?,
    @ColumnInfo(name = "request_url_encoded_fragment") val requestEncodedFragment: String?,
    @ColumnInfo(name = "request_url_encoded_password") val requestEncodedPassword: String?,
    @ColumnInfo(name = "request_url_encoded_path") val requestEncodedPath: String,
    @ColumnInfo(name = "request_url_encoded_path_segments") val requestEncodedPathSegments: List<String>?,
    @ColumnInfo(name = "request_url_encoded_query") val requestEncodedQuery: String?,
    @ColumnInfo(name = "request_url_encoded_username") val requestEncodedUserName: String?,
    @ColumnInfo(name = "request_url_fragment") val requestFragment: String?,
    @ColumnInfo(name = "request_url_password") val requestPassword: String?,
    @ColumnInfo(name = "request_url_path_segments") val requestPathSegments: List<String>?,
    @ColumnInfo(name = "request_url_path_size") val requestPathSize: Int?,
    @ColumnInfo(name = "request_url_query") val requestQuery: String?,
    @ColumnInfo(name = "request_url_query_parameter_name") val requestQueryParameterName: String?,
    @ColumnInfo(name = "request_url_query_size") val requestQuerySize: Int?,
    @ColumnInfo(name = "request_url_port") val requestPort: Int?,
    @ColumnInfo(name = "request_url_username") val requestUsername: String?,
    @ColumnInfo(name = "request_body") val requestBody: String?,
    @ColumnInfo(name = "request_body_content_length") val requestContentLength: Long?,
    @ColumnInfo(name = "request_body_content_type") val requestContentType: String?,
    @ColumnInfo(name = "request_headers") val requestHeaders: String?,
)
