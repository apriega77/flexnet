package com.flexnet.presentation.feature.di

import android.content.Context
import com.flexnet.api.FlexNetProperties
import com.flexnet.data.di.Bind
import com.flexnet.data.di.Provide
import com.flexnet.data.interceptor.FlexNetInterceptorImpl
import com.flexnet.presentation.feature.notification.NotificationHelper
import com.flexnet.presentation.feature.notification.NotificationModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        Bind::class,
        Provide::class,
        ViewModelModule::class,
        ViewModelFactoryModule::class,
        NotificationModule::class,
    ],
)
internal interface FlexNetComponent {
    fun getFlexNetInterceptor(): FlexNetInterceptorImpl

    fun getNotificationHelper(): NotificationHelper

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance flexNetProperties: FlexNetProperties,
        ): FlexNetComponent
    }
}
