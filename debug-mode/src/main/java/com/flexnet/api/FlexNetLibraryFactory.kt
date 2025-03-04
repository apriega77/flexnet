package com.flexnet.api

import android.content.Context
import com.flexnet.presentation.feature.di.DaggerFlexNetComponent

object FlexNetLibraryFactory {
    fun create(context: Context, flexNetProperties: FlexNetProperties): FlexNetInterceptor {
        val daggerComponent = DaggerFlexNetComponent.factory().create(context, flexNetProperties)
        val notificationHelper = daggerComponent.getNotificationHelper()
        if (flexNetProperties.showNotification) {
            notificationHelper.showNotification(
                "FlexNet",
                "Click to open",
            )
        }
        return daggerComponent.getFlexNetInterceptor()
    }
}
