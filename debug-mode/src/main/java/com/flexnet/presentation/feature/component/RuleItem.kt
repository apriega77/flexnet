package com.flexnet.presentation.feature.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flexnet.domain.model.Method
import com.flexnet.domain.model.NetworkRule

@Composable
internal fun RuleItem(
    modifier: Modifier = Modifier,
    networkRule: NetworkRule,
    lastItem: Boolean,
    onRuleClick: (NetworkRule) -> Unit,
    onToggleChange: (Boolean, NetworkRule) -> Unit,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onRuleClick(networkRule)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1F),
            ) {
                Text(
                    text = networkRule.title,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                )
                Text(text = networkRule.url)
            }
            Switch(
                modifier = Modifier.padding(16.dp),
                checked = networkRule.isActive,
                onCheckedChange = {
                    onToggleChange(it, networkRule)
                },
            )
        }

        if (!lastItem) {
            HorizontalDivider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewRuleItem() {
    RuleItem(
        networkRule = NetworkRule(
            id = 123,
            title = "Home Api",
            isActive = true,
            url = "www.home.api",
            method = Method.GET,
            httpCode = 200,
            responseBody = "",
        ),
        lastItem = false,
        onRuleClick = {},
        onToggleChange = { _, _ ->
        },
    )
}
