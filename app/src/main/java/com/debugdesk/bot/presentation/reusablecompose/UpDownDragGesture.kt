package com.debugdesk.bot.presentation.reusablecompose

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.R
import com.debugdesk.bot.presentation.screens.botapp.BotAppViewModel
import kotlinx.coroutines.delay

@Composable
fun UpDownDragGesture(
    isVisible: Boolean,
) {
    var isSizeNormal by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(targetValue = if (isSizeNormal) 150.dp else 170.dp, label = "Size")
    val angle by animateFloatAsState(targetValue = if (isSizeNormal) 5f else -5f, label = "Size")

    LaunchedEffect(isSizeNormal) {
        delay(400)
        isSizeNormal = !isSizeNormal
    }

    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(
            animationSpec = tween(delayMillis = 500)
        ) + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(delayMillis = 500)
        ),
        exit = fadeOut(
            animationSpec = tween(delayMillis = 500)
        ) + slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(delayMillis = 500)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.swipe_vertical),
                    contentDescription = "Gesture",
                    modifier = Modifier
                        .size(size)
                        .rotate(angle),
                    colorFilter = ColorFilter.tint(color = Color.White)
                )

                SpacerHeight(value = 20)

                Text(
                    text = "Swipe Vertically",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                )
            }

        }
    }

}