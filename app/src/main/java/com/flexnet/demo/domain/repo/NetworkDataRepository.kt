package com.flexnet.demo.domain.repo

import com.flexnet.demo.domain.entities.PostDataModel
import kotlinx.coroutines.flow.Flow


interface NetworkDataRepository {
    suspend fun getPosts(): Flow<List<PostDataModel>>
    suspend fun getPost(id: String): Flow<PostDataModel>
}