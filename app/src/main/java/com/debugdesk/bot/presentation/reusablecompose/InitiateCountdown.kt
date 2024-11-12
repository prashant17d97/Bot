package com.debugdesk.bot.presentation.reusablecompose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.R
import com.debugdesk.bot.datamodel.InitCountDownTimer
import com.debugdesk.bot.datamodel.TimerParameter
import com.debugdesk.bot.utils.timeutil.DisplayMode
import com.debugdesk.bot.utils.timeutil.TimeUnitEnum

@Composable
fun InitiateCountdown(
    modifier: Modifier = Modifier,
    countDownTimer: InitCountDownTimer = InitCountDownTimer(),
    onStart: (TimerParameter) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val context = LocalContext.current
    var selectedTimeUnitIndex by remember { mutableIntStateOf(-1) }
    var selectedDisplayModeIndex by remember { mutableIntStateOf(-1) }
    var timerParameter by remember { mutableStateOf(TimerParameter()) }


    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.Top
        ),
        horizontalAlignment = Alignment.Start
    ) {
        LargeDropdownMenu(
            label = stringResource(id = R.string.select_time_unit),
            items = countDownTimer.timeUnitEnums,
            selectedIndex = selectedTimeUnitIndex,
            onItemSelected = { index, timeUnitEnum ->
                selectedTimeUnitIndex = index
                timerParameter = timerParameter.copy(timeUnitEnum = timeUnitEnum)
            },
            selectedItemToString = { context.getString(it.textValue) },
            drawItem = { timeUnitEnum: TimeUnitEnum, selected: Boolean, itemEnabled: Boolean, onClick: () -> Unit ->
                LargeDropdownMenuItem(
                    text = stringResource(id = timeUnitEnum.textValue),
                    selected = selected,
                    enabled = itemEnabled,
                    onClick = onClick,
                )
            }
        )

        LargeDropdownMenu(
            label = stringResource(id = R.string.select_display_mode),
            items = countDownTimer.displayMode,
            selectedIndex = selectedDisplayModeIndex,
            onItemSelected = { index, displayMode ->
                selectedDisplayModeIndex = index
                timerParameter = timerParameter.copy(displayMode = displayMode)

            },
            selectedItemToString = { context.getString(it.textValue) },
            drawItem = { displayMode: DisplayMode, selected: Boolean, itemEnabled: Boolean, onClick: () -> Unit ->
                LargeDropdownMenuItem(
                    text = stringResource(id = displayMode.textValue),
                    selected = selected,
                    enabled = itemEnabled,
                    onClick = onClick,
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            value = timerParameter.timeValue,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            textStyle = MaterialTheme.typography.titleSmall,
            placeholder = { Text(text = stringResource(id = R.string.timeValue)) },
            onValueChange = { timerParameter = timerParameter.copy(timeValue = it.digitsOnly()) },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.End)
        ) {
            Button(onClick = { onCancel() }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Button(
                onClick = {
                    context.validate(timerParameter, onValidate = onStart)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

private fun Context.validate(timerParameter: TimerParameter, onValidate: (TimerParameter) -> Unit) {

    val errorMessage = when {
        timerParameter.timeUnitEnum == TimeUnitEnum.NOTHING ->
            getString(R.string.invalid_time_unit)

        timerParameter.displayMode == DisplayMode.NOTHING ->
            getString(R.string.invalid_display_mode)

        timerParameter.timeValue.isEmpty() ->
            getString(R.string.invalid_time_value)

        else -> {
            onValidate(timerParameter)
            ""
        }
    }
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}

@Preview
@Composable
private fun TimeInitPre() {
    InitiateCountdown()
}

fun String.digitsOnly(): String {
    return try {
        this.filter { it.isDigit() }
    } catch (ex: Exception) {
        ""
    }
}