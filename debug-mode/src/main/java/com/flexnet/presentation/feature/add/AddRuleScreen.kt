package com.flexnet.presentation.feature.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.flexnet.domain.model.Method
import com.flexnet.presentation.foundation.FlexNetColorScheme

@Composable
internal fun AddRuleScreen(state: AddRuleState, event: (AddRuleEvent) -> Unit) {
    val showDeleteButton by rememberUpdatedState(newValue = state is AddRuleState.Detail)

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = state.titleState,
            onValueChange = {
                event(AddRuleEvent.OnTitleChange(it))
            },
            label = { Text(text = "Title") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Active", modifier = Modifier.weight(1f))

            Switch(
                checked = state.isActiveState,
                onCheckedChange = {
                    event(AddRuleEvent.OnToggleChange(it))
                },
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.urlState,
            onValueChange = {
                event(AddRuleEvent.OnUrlChange(it))
            },
            label = { Text(text = "URL") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            OutlinedTextField(
                value = state.methodState.name,
                readOnly = true,
                onValueChange = {
                },
                label = { Text(text = "Method") },
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Image(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                        )
                    }
                },
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Method.entries.toTypedArray().onEach {
                    DropdownMenuItem(
                        onClick = {
                            event(AddRuleEvent.OnMethodItemChange(it))
                            expanded = false
                        },
                        text = { Text(text = it.name) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = state.httpCodeState,
            onValueChange = {
                event(AddRuleEvent.OnHttpCodeChange(it))
            },
            label = { Text(text = "HTTP Code") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state.responseBodyState,
            onValueChange = {
                event(AddRuleEvent.OnResponseBodyChange(it))
            },
            label = { Text(text = "Response Body") },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .weight(1F),
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { event(AddRuleEvent.SaveRule) },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),

        ) {
            Text(text = "Save")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showDeleteButton) {
            Button(
                onClick = { event(AddRuleEvent.DeleteRule) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonColors(
                    containerColor = FlexNetColorScheme.errorContainer,
                    contentColor = FlexNetColorScheme.onError,
                    disabledContainerColor = FlexNetColorScheme.onTertiaryContainer,
                    disabledContentColor = FlexNetColorScheme.onTertiary,
                ),
            ) {
                Text(text = "Delete")
            }
        }
    }
}
