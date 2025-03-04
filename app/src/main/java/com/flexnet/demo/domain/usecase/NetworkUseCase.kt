package com.flexnet.demo.domain.usecase

import com.flexnet.demo.domain.entities.PostDataModel
import com.flexnet.demo.domain.repo.NetworkDataRepository
import kotlinx.coroutines.flow.Flow


class NetworkUseCase(var repo: NetworkDataRepository) {
    suspend fun getPosts(): Flow<List<PostDataModel>> = repo.getPosts()
    suspend fun getPost(id: String): Flow<PostDataModel> = repo.getPost(id)

}