package com.flexnet.data.repository

import com.flexnet.data.mapper.HttpInspectorMapper
import com.flexnet.data.room.dao.HttpInspectorDao
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import com.flexnet.domain.model.HttpInspector
import com.flexnet.domain.repository.HttpInspectorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class HttpInspectorRepositoryImpl @Inject constructor(
    private val httpInspectorDao: HttpInspectorDao,
    private val httpInspectorMapper: HttpInspectorMapper,
) :
    HttpInspectorRepository {
    override suspend fun getHttpInspector(searchUrl: String): Flow<List<HttpInspector>> {
        return httpInspectorDao.getHttpInspector(searchUrl).map {
            httpInspectorMapper.mapHttpInspectorList(it)
        }
    }

    override suspend fun saveHttpInspector(httpInspectorEntity: HttpInspectorEntity) {
        val id = httpInspectorDao.saveHttpInspector(httpInspectorEntity)
        httpInspectorEntity.id = id
    }

    override suspend fun deleteAllHttpInspector() {
        httpInspectorDao.deleteAllHttpInspector()
    }

    override suspend fun deleteHttpInspector(id: String) {
        httpInspectorDao.deleteHttpInspector(id.toLong())
    }
}
