package com.flexnet.presentation.foundation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun FlexNetTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = FlexNetColorScheme,
        typography = FlexNetTypography,
        shapes = FlexNetShape,
    ) {
        content()
    }
}
