package com.debugdesk.bot.presentation.reusablecompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.ui.theme.BotTheme

@Composable
fun CircularProgressBarWithGradient(progress: Float) {
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
    ) {
        val center = size.width / 2f
        val radius = size.width / 2f
        val startAngle = 0f
        val sweepAngle = 360f * progress

        // Define a radial gradient
        val gradient = Brush.linearGradient(
            colors = listOf(Color.Blue, Color.Green, Color.Red, Color.Cyan, Color.Yellow),
        )

        // Draw the background circle
        /*drawCircle(
            brush = gradient,
            center = Offset(center, center),
            radius = radius,
            style = Stroke(8f)
        )*/

        // Draw the progress arc
        drawArc(
            brush = gradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(8f)
        )
    }
}

@Composable
fun CircularProgressBarWithGradientExample() {
    var progress by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CircularProgressBarWithGradient(progress = progress)

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = progress,
            onValueChange = {
                progress = it
            },
            valueRange = 0f..1f,
            steps = 100
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircularProgressBarWithGradientExamplePreview() {
    BotTheme {
        CircularProgressBarWithGradientExample()
    }
}
