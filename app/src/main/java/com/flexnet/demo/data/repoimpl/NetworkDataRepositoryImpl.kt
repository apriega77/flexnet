package com.flexnet.demo.data.repoimpl

import com.flexnet.demo.data.network.RetrofitAPI
import com.flexnet.demo.domain.entities.PostDataModel
import com.flexnet.demo.domain.repo.NetworkDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkDataRepositoryImpl @Inject constructor(private val api: RetrofitAPI) :
    NetworkDataRepository {

    override suspend fun getPosts(): Flow<List<PostDataModel>> {
        return flow {
            emit(api.getPosts())
        }
    }

    override suspend fun getPost(id: String): Flow<PostDataModel> {
        return flow {
            emit(api.getPost(id))
        }
    }

}