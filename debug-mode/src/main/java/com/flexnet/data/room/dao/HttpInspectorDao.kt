package com.flexnet.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.flexnet.data.room.entity.httpinspector.HttpInspectorEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface HttpInspectorDao {
    @Query(
        """
    SELECT hi.*,
           CASE WHEN nr.url IS NOT NULL THEN 1 ELSE 0 END AS is_mocked
    FROM http_inspector AS hi
    LEFT JOIN network_rules AS nr ON hi.request_url = nr.url
    AND hi.request_method = nr.method
    AND nr.is_active
    WHERE (:url = '' OR hi.request_url LIKE '%' || :url || '%')
    ORDER BY hi.created_at DESC
""",
    )
    fun getHttpInspector(url: String): Flow<List<HttpInspectorEntity>>

    @Query("DELETE FROM http_inspector")
    suspend fun deleteAllHttpInspector()

    @Query("DELETE FROM http_inspector WHERE :id = id")
    suspend fun deleteHttpInspector(id: Long)

    @Upsert
    suspend fun saveHttpInspector(httpInspectorEntity: HttpInspectorEntity): Long
}
