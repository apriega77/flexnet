package com.flexnet.domain.repository

import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.domain.model.HttpInspector
import kotlinx.coroutines.flow.Flow

internal interface HttpInspectorRepository {
    suspend fun getHttpInspector(searchUrl: String): Flow<List<HttpInspector>>
    suspend fun saveHttpInspector(httpInspectorEntity: HttpInspectorEntity)
    suspend fun deleteAllHttpInspector()
    suspend fun deleteHttpInspector(id: String)
}
