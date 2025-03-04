package com.flexnet.data.repository

import com.flexnet.data.mapper.NetworkRuleMapper
import com.flexnet.data.room.dao.NetworkRuleDao
import com.flexnet.domain.model.NetworkRule
import com.flexnet.domain.repository.NetworkRuleRepository
import javax.inject.Inject

internal class NetworkRuleRepositoryImpl @Inject constructor(
    private val networkRuleDao: NetworkRuleDao,
    private val mapper: NetworkRuleMapper,
) : NetworkRuleRepository {
    override suspend fun getRule(): List<NetworkRule> {
        return networkRuleDao.getAllRules().map {
            mapper.mapNetworkRule(it)
        }
    }
    override suspend fun deleteRule(args: NetworkRule) {
        networkRuleDao.deleteRule(
            mapper.mapNetworkRuleEntity(args),
        )
    }
    override suspend fun saveRule(args: NetworkRule) {
        networkRuleDao.saveRule(
            mapper.mapNetworkRuleEntity(args),
        )
    }
}
