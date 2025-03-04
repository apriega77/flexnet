package com.flexnet.data.room.entity.rule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_rules")
internal data class NetworkRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "is_active") val isActive: Boolean?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "method") val method: String?,
    @ColumnInfo(name = "http_code") val httpCode: Int?,
    @ColumnInfo(name = "response_body") val responseBody: String?,
)
