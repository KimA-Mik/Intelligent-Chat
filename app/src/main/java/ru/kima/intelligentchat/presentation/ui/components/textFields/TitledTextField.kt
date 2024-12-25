package ru.kima.intelligentchat.presentation.ui.components.textFields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.util.preview.ICPreview

@Composable
fun TitledTextField(
    value: String,
    inputValue: (String) -> Unit,
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row {
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineSmall) {
                title()
            }
        }
        OutlinedTextField(
            value = value, onValueChange = inputValue,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun TitledTextFieldPreview() = ICPreview {
    TitledTextField(
        value = "value",
        inputValue = {},
        title = {
            Text("Title")
        },
        modifier = Modifier.padding(8.dp)
    )
}