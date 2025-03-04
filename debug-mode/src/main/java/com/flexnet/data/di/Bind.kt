package com.flexnet.data.di

import com.flexnet.api.FlexNetInterceptor
import com.flexnet.data.interceptor.FlexNetInterceptorImpl
import com.flexnet.data.repository.HttpInspectorRepositoryImpl
import com.flexnet.data.repository.NetworkRuleRepositoryImpl
import com.flexnet.domain.repository.HttpInspectorRepository
import com.flexnet.domain.repository.NetworkRuleRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class Bind {
    @Binds
    @Singleton
    abstract fun bindRepo(
        flexNetRepositoryImpl: NetworkRuleRepositoryImpl,
    ): NetworkRuleRepository

    @Binds
    @Singleton
    abstract fun bindFlexNetInterceptorImpl(interceptor: FlexNetInterceptorImpl): FlexNetInterceptor

    @Binds
    @Singleton
    abstract fun bindHttpInspectorImpl(httpInspectorRepositoryImpl: HttpInspectorRepositoryImpl): HttpInspectorRepository
}
