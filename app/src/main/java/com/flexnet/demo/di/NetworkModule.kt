package com.flexnet.demo.di

import android.content.Context
import com.flexnet.api.FlexNetInterceptor
import com.flexnet.api.FlexNetLibraryFactory
import com.flexnet.api.FlexNetProperties
import com.flexnet.demo.data.network.Network
import com.flexnet.demo.data.network.RetrofitAPI
import com.flexnet.demo.data.repoimpl.NetworkDataRepositoryImpl
import com.flexnet.demo.domain.entities.NetworkDataViewModel
import com.flexnet.demo.domain.repo.NetworkDataRepository
import com.flexnet.demo.domain.usecase.NetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class NetworkModule {

    @Provides
    @ViewModelScoped
    fun provideRetrofitAPI(flexNetInterceptor: FlexNetInterceptor): RetrofitAPI {
        // this is just a demo how you can manage your retrofit class
        // you can add custom base url,headers and even create api service from here for every custom modules
        return Network.init(flexNetInterceptor).apiService
    }

    @Provides
    @ViewModelScoped
    fun provideNetworkRepository(api: RetrofitAPI): NetworkDataRepository =
        NetworkDataRepositoryImpl(api)

    @Provides
    @ViewModelScoped
    fun provideNetworkUseCase(repository: NetworkDataRepository): NetworkUseCase =
        NetworkUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideNetworkDataViewModel(useCase: NetworkUseCase): NetworkDataViewModel =
        NetworkDataViewModel(useCase)

    @Provides
    @ViewModelScoped
    fun provideFlexNetInterceptor(@ApplicationContext context: Context): FlexNetInterceptor {
        return FlexNetLibraryFactory.create(context, FlexNetProperties(true))
    }
}