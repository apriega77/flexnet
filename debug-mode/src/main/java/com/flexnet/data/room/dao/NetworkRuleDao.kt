package com.flexnet.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.flexnet.data.room.entity.rule.NetworkRuleEntity

@Dao
internal interface NetworkRuleDao {
    @Query("SELECT * FROM network_rules")
    suspend fun getAllRules(): List<NetworkRuleEntity>

    @Delete
    suspend fun deleteRule(rule: NetworkRuleEntity)

    @Upsert
    suspend fun saveRule(rule: NetworkRuleEntity)
}
