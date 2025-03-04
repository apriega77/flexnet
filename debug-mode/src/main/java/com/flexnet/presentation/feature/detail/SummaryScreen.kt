package com.flexnet.presentation.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flexnet.domain.model.HttpInspectorSummary
import com.flexnet.domain.model.KeyValue
import com.flexnet.presentation.feature.component.DetailComponent
import com.flexnet.presentation.feature.component.empty.EmptyScreen

@Composable
fun SummaryScreen(data: HttpInspectorSummary) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD)),
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
        items(data.summary) {
            DetailComponent(keyValue = it, modifier = Modifier.padding(top = 12.dp))
        }
    }

    val showEmptyScreen by remember { mutableStateOf(data.summary.isEmpty()) }
    if (showEmptyScreen) {
        EmptyScreen()
    }
}

@Composable
@Preview
private fun PreviewSummaryScreen() {
    SummaryScreen(
        data = HttpInspectorSummary(
            listOf(
                listOf(
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                ),
                listOf(
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                ),

            ),

        ),
    )
}
