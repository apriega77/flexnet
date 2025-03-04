package com.flexnet.presentation.feature

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.flexnet.presentation.feature.di.DaggerViewModelComponent
import com.flexnet.presentation.feature.di.ViewModelComponent
import com.flexnet.presentation.feature.notification.HttpInspectorNotificationArgs
import com.flexnet.presentation.foundation.FlexNetTheme
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class FlexNetActivity : ComponentActivity() {

    val httpInspectorNotificationArgs by parcelableArgs<HttpInspectorNotificationArgs>()

    internal lateinit var viewModelComponent: ViewModelComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelComponent = DaggerViewModelComponent.factory().create(this)
        setContent {
            FlexNetTheme {
                FlexNetMainScreen()
            }
        }
    }

    private inline fun <reified Args : Parcelable> Activity.parcelableArgs(): ReadOnlyProperty<Activity, Args?> {
        return object : ReadOnlyProperty<Activity, Args?> {
            private var value: Args? = null
            private var initiated: Boolean = false

            override fun getValue(thisRef: Activity, property: KProperty<*>): Args? {
                if (!initiated) {
                    value = try {
                        val retrievedValue = intent.getParcelableExtra<Args>("contract.intent.args")
                        initiated = true
                        retrievedValue
                    } catch (e: Exception) {
                        null
                    }
                }

                // Clear the value after retrieving it
                val result = value
                value = null // Reset the value for the next access
                return result
            }
        }
    }
}
