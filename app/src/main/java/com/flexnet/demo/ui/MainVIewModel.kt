package com.flexnet.demo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexnet.demo.domain.entities.PostDataModel
import com.flexnet.demo.domain.usecase.NetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVIewModel @Inject constructor(private val networkUseCase: NetworkUseCase) : ViewModel() {

    private var _postList = MutableStateFlow<List<PostDataModel>>(emptyList())
    var postList: StateFlow<List<PostDataModel>> = _postList

    var errorMessage = MutableStateFlow("")
    var isLoading = MutableStateFlow(false)

    fun getPosts() {
        viewModelScope.launch {

            isLoading.value = true

            networkUseCase.getPosts().catch { error ->
                errorMessage.value = error.message ?: ""
                isLoading.value = false
            }.collect {
                _postList.value = it
                isLoading.value = false
            }
        }
    }

    fun getPost(id: String) {
        viewModelScope.launch {
            isLoading.value = true

            networkUseCase.getPost(id).catch { error ->
                errorMessage.value = error.message ?: ""
                isLoading.value = false

            }.collect {
                _postList.value = arrayListOf(it)
                isLoading.value = false
            }
        }
    }

}