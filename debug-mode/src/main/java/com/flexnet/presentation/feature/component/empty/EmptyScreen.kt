package com.flexnet.presentation.feature.component.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.flexnet.R

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Image(
            painter = painterResource(R.drawable.img_empty),
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
        )
    }
}
