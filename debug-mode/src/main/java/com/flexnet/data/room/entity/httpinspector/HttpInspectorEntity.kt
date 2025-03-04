package com.flexnet.data.room.entity.httpinspector

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "http_inspector")
data class HttpInspectorEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo("state") val state: String = "",
    @Embedded val httpRequestInspectorEntity: HttpRequestInspectorEntity? = null,
    @Embedded val httpResponseInspectorEntity: HttpResponseInspectorEntity? = null,
    @ColumnInfo("error") val error: String? = null,
    @ColumnInfo("created_at") val createdAt: String?,
    @ColumnInfo("is_mocked") val isMocked: Boolean? = false,
)
