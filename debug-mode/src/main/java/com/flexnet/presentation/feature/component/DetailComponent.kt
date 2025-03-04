package com.flexnet.presentation.feature.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flexnet.domain.model.KeyValue
import com.flexnet.presentation.foundation.FlexNetTheme
import com.flexnet.presentation.foundation.FlexNetTypography

@Composable
fun DetailComponent(modifier: Modifier = Modifier, keyValue: List<KeyValue>) {
    Column(
        modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .background(Color.White),
    ) {
        keyValue.forEachIndexed { index, item ->

            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp),
            ) {
                Text(
                    text = item.key,
                    modifier = Modifier.fillMaxWidth(),
                    style = FlexNetTypography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                )

                Text(
                    text = item.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    style = FlexNetTypography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF989CA6),
                )

                Spacer(Modifier.size(8.dp))

                if (index != keyValue.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF2F4F7),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun PreviewDetailComponent() {
    FlexNetTheme {
        DetailComponent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyValue = listOf(
                KeyValue(key = "consectetuer", value = "ridiculus"),
                KeyValue(key = "consectetuer", value = "ridiculus"),
                KeyValue(key = "consectetuer", value = "ridiculus"),
                KeyValue(key = "consectetuer", value = "ridiculus"),
                KeyValue(key = "consectetuer", value = "ridiculus"),
            ),
        )
    }
}
