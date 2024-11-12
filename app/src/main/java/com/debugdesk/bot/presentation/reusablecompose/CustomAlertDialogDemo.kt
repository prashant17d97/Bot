package com.debugdesk.bot.presentation.reusablecompose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.R

@Composable
fun CustomAlertDialog(
    showDialog: Boolean = true,
    title: String,
    text: String,
    dismissButtonText: String = stringResource(id = R.string.cancel),
    confirmButtonText: String = stringResource(id = R.string.yes),
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text(text = dismissButtonText)
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.Start)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bot_transparent),
                        modifier = Modifier.size(50.dp),
                        contentDescription = "Icon"
                    )
                    Text(text = title)
                }
            },
            text = { Text(text = text) },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview
@Composable
private fun AlertPrev() {
    CustomAlertDialog(title = "Alert", text = "Are you sure, You want to delete note?")
}