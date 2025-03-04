package com.flexnet.presentation.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flexnet.domain.model.HttpInspectorResponse
import com.flexnet.domain.model.KeyValue
import com.flexnet.presentation.feature.component.DetailComponent
import com.flexnet.presentation.feature.component.empty.EmptyScreen
import com.flexnet.presentation.feature.component.jsonviewer.JsonParser
import com.flexnet.presentation.feature.component.jsonviewer.JsonViewerWidget
import com.flexnet.presentation.foundation.FlexNetTheme

@Composable
fun ResponseScreen(response: HttpInspectorResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FD)),
    ) {
        if (response.header.isNotEmpty()) {
            DetailComponent(
                keyValue = response.header,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 16.dp),
            )
        }

        if (response.bodyJson.isNotEmpty()) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
            ) {
                JsonViewerWidget(response.bodyJson, modifier = Modifier)
            }
        }
    }

    val showEmptyScreen by remember { mutableStateOf(response.bodyJson.isEmpty() && response.header.isEmpty()) }
    if (showEmptyScreen) {
        EmptyScreen()
    }
}

@Composable
@Preview
private fun PreviewRequestScreen() {
    val json = "{\n" +
        "  \"userId\": 12345,\n" +
        "  \"firstName\": \"John\",\n" +
        "  \"lastName\": \"Doe\",\n" +
        "  \"email\": \"johndoe@example.com\",\n" +
        "  \"profilePictureUrl\": \"https://example.com/profile-pictures/johndoe.jpg\",\n" +
        "  \"phoneNumber\": \"+1-555-123-4567\",\n" +
        "  \"address\": {\n" +
        "    \"street\": \"123 Main St\",\n" +
        "    \"city\": \"New York\",\n" +
        "    \"state\": \"NY\",\n" +
        "    \"zipCode\": \"10001\",\n" +
        "    \"country\": \"USA\"\n" +
        "  },\n" +
        "  \"dateOfBirth\": \"1990-01-01\",\n" +
        "  \"accountCreated\": \"2020-05-15T08:30:00Z\",\n" +
        "  \"lastLogin\": \"2024-10-03T10:15:00Z\",\n" +
        "  \"preferences\": {\n" +
        "    \"language\": \"en\",\n" +
        "    \"currency\": \"USD\",\n" +
        "    \"darkMode\": true\n" +
        "  },\n" +
        "  \"subscription\": {\n" +
        "    \"status\": \"active\",\n" +
        "    \"plan\": \"premium\",\n" +
        "    \"renewalDate\": \"2025-05-15\"\n" +
        "  }\n" +
        "}\n"

    FlexNetTheme {
        ResponseScreen(
            response = HttpInspectorResponse(
                header = listOf(
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                    KeyValue(key = "consectetuer", value = "ridiculus"),
                ),
                bodyJson = JsonParser().parse(json),
                bodyString = json,
            ),
        )
    }
}
