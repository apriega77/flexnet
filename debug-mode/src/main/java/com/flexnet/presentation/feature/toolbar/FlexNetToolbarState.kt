package com.flexnet.presentation.feature.toolbar

import androidx.compose.runtime.Composable

internal sealed interface FlexNetToolbarState {
    val title: String
    val hasBackButton: Boolean
    val trailingIcon: @Composable (() -> Unit)?

    data class MainScreen(
        override val trailingIcon:
        @Composable()
        (() -> Unit)?,
    ) :
        FlexNetToolbarState {
        override val title: String
            get() = "FlexNet"
        override val hasBackButton: Boolean
            get() = false
    }

    data object AddRule : FlexNetToolbarState {
        override val title: String
            get() = "Add Network Rule"
        override val hasBackButton: Boolean
            get() = true
        override val trailingIcon: @Composable (() -> Unit)?
            get() = null
    }

    data class Detail(
        override val title: String,
        override val trailingIcon:
        @Composable()
        (() -> Unit)?,
    ) : FlexNetToolbarState {
        override val hasBackButton: Boolean
            get() = true
    }
}
