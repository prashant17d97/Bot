package com.debugdesk.bot.presentation.screens.remainderprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun RemainderProfile(modifier: Modifier = Modifier, navHostController: NavHostController) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(10.dp)) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                space = 10.dp,
                alignment = Alignment.Top
            )
        ) {
            items(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) {
                RemainderCard()
            }
            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            onClick = {},
            shadowElevation = 10.dp,
            tonalElevation = 10.dp,
            shape = CircleShape,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .size(80.dp)

        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
private fun RemainderProfilePrev(modifier: Modifier = Modifier) {
    RemainderProfile(navHostController = rememberNavController())
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun RemainderCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            IconToggleButton(checked = true, onCheckedChange = {}) {
                Text(text = "Label")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "EveryDay")
                Switch(checked = true, onCheckedChange = {})
            }
        }
    }
}

@Composable
private fun IconText(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    text: @Composable () -> Unit
) {
    Surface {
        Row() {
            leadingIcon?.let { it() }
            text()
            trailingIcon?.let { it() }
        }
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun ScreenOrientationObserver(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}