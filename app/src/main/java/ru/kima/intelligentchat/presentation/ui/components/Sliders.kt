package ru.kima.intelligentchat.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.kima.intelligentchat.common.format
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

//TODO: Fix sliders input field, https://stackoverflow.com/questions/70645803/how-to-detect-if-the-user-stops-writing-to-a-textfield
//for example

private val defaultToolTipIcon = Icons.Outlined.Info

//TODO: Fix text field width being outweigh by title
@Composable
fun TitledFiniteSlider(
    title: String,
    value: Int,
    leftBorder: Int,
    rightBorder: Int,
    updateValue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textFieldLabel: String? = null,
    tooltipIcon: ImageVector = defaultToolTipIcon,
    tooltipText: String? = null
) {
    Column(modifier) {
        val sliderRange = remember(leftBorder, rightBorder) {
            leftBorder.toFloat()..rightBorder.toFloat()
        }
        var sliderValue by remember(value) {
            mutableFloatStateOf(value.toFloat())
        }
        var textFieldValue by remember(value) {
            mutableStateOf(value.toString())
        }
        var textFieldDebounceValue by remember {
            mutableStateOf(textFieldValue)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            tooltipText?.let {
                TooltipIconButton(
                    text = tooltipText,
                    icon = tooltipIcon
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    textFieldDebounceValue = it
                },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.End
                ),
                label = {
                    textFieldLabel?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
//                modifier = Modifier.widthIn(64.dp, 280.dp)
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 8.dp)
            )
        }
        val steps = remember(leftBorder, rightBorder) {
            rightBorder - leftBorder
        }

        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                textFieldValue = it.toInt().toString()
            },
            valueRange = sliderRange,
            steps = steps,
            onValueChangeFinished = { updateValue(sliderValue.toInt()) }
        )

        LaunchedEffect(key1 = textFieldDebounceValue) {
            var newValue = textFieldDebounceValue
                .trimEnd(',', '.')
                .toIntOrNull()
                ?: Int.MIN_VALUE

            if (newValue == value) return@LaunchedEffect

            if (newValue < leftBorder) newValue = leftBorder
            if (newValue > rightBorder) newValue = rightBorder
            sliderValue = newValue.toFloat()

            delay(1000)
            if (newValue == value) {
                textFieldValue = newValue.toString()
            }
            updateValue(newValue)
        }
    }
}

@Composable
fun TitledFloatSlider(
    title: String,
    value: Float,
    leftBorder: Float,
    rightBorder: Float,
    updateValue: (Float) -> Unit,
    modifier: Modifier = Modifier,
    textFieldLabel: String? = null,
    tooltipIcon: ImageVector = defaultToolTipIcon,
    tooltipText: String? = null
) {
    Column(modifier) {
        val sliderRange = remember(leftBorder, rightBorder) {
            leftBorder..rightBorder
        }
        var sliderValue by remember(value) {
            mutableFloatStateOf(value)
        }
        var textFieldValue by remember(value) {
            mutableStateOf(value.format(2))
        }
        var textFieldDebounceValue by remember {
            mutableStateOf(String())
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            tooltipText?.let {
                TooltipIconButton(
                    text = tooltipText,
                    icon = tooltipIcon
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            OutlinedTextField(
                value = textFieldValue, onValueChange = {
                    textFieldValue = it
                    textFieldDebounceValue = it
                },
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.End
                ),
                label = {
                    textFieldLabel?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 8.dp)
            )
        }

        Slider(
            value = sliderValue,
            onValueChange = {
                sliderValue = it
                textFieldValue = it.format(2)
            },
            valueRange = sliderRange,
            onValueChangeFinished = { updateValue(sliderValue) }
        )

        //TODO: Read about float formatting
        LaunchedEffect(key1 = textFieldDebounceValue) {
            if (textFieldDebounceValue.isBlank()) return@LaunchedEffect
            var newValue = textFieldDebounceValue
                .replace(',', '.')
                .toFloatOrNull()
                ?: -(Float.MAX_VALUE - 1f)

            if (newValue < leftBorder) newValue = leftBorder
            if (newValue > rightBorder) newValue = rightBorder
            sliderValue = newValue

            delay(1000)
            if (newValue == value) {
                textFieldValue = newValue.format(2)
            }
            updateValue(newValue)
        }
    }
}

@Composable
@Preview
fun TitledFiniteSliderPreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TitledFiniteSlider(
                title = "Title", value = 25, leftBorder = 0, rightBorder = 50, updateValue = {},
                modifier = Modifier.padding(16.dp),
                tooltipText = "Tooltip"
            )
        }
    }
}

@Composable
@Preview
fun TitledFloatSliderPreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TitledFloatSlider(
                title = "Title",
                value = 20.5f,
                leftBorder = 0f,
                rightBorder = 50f,
                updateValue = {},
                modifier = Modifier.padding(16.dp),
                tooltipText = "Tooltip"
            )
        }
    }
}