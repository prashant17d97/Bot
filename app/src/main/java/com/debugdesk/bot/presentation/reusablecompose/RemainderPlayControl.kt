package com.debugdesk.bot.presentation.reusablecompose

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.R
import com.debugdesk.bot.utils.timeutil.TimerState
import com.debugdesk.bot.ui.theme.BotTheme

@Composable
fun RemainderPlayControl(
    modifier: Modifier = Modifier,
    currentTimerState: TimerState = TimerState.NotInitialized,
    onClick: (TimerState) -> Unit = {}
) {
    val isVisible =
        currentTimerState == TimerState.NotInitialized || currentTimerState == TimerState.Finished

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            10.dp, alignment = Alignment.CenterHorizontally
        )
    ) {
        AnimatedVisibility(visible = isVisible) {
            Button(
                onClick = { onClick(TimerState.Started) },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 12.dp,
                    pressedElevation = 12.dp,
                    focusedElevation = 12.dp,
                    hoveredElevation = 12.dp,
                    disabledElevation = 12.dp
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.start_remainder),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (!isVisible) {
            IconButton(onClick = { onClick(TimerState.Replay) }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_replay_24),
                    contentDescription = "Replay",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(onClick = {
                onClick(
                    if (currentTimerState == TimerState.Started || currentTimerState == TimerState.Resume)
                        TimerState.Paused
                    else TimerState.Resume
                )
            }) {
                Icon(
                    painter = painterResource(id = if (currentTimerState == TimerState.Started || currentTimerState == TimerState.Resume) R.drawable.icon_pause else R.drawable.icon_play),
                    contentDescription = "Replay",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )
            }

            IconButton(onClick = { onClick(TimerState.Cancel) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel),
                    contentDescription = "Replay",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }

}

@Preview
@Composable
private fun RemainderPlayControlPreV() {
    BotTheme {
        RemainderPlayControl() {
            Log.e("TAG", "RemainderPlayControlPreV: $it")
        }
    }
}