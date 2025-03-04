package com.flexnet.presentation.feature.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationHelper(context: Context): NotificationHelper {
        return NotificationHelper(context)
    }
}
