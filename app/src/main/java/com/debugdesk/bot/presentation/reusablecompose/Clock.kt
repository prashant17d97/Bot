package com.debugdesk.bot.presentation.reusablecompose

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.ui.theme.BotTheme
import com.debugdesk.bot.utils.timeutil.Time
import com.debugdesk.bot.utils.timeutil.TimerState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    clock: Time,
    timer: Time,
    currentTimerState: TimerState = TimerState.NotInitialized,
    onClick: (TimerState) -> Unit = {}
) {
    val color = MaterialTheme.colorScheme.secondary
    val background = MaterialTheme.colorScheme.background

    val colorGradient = listOf(
        Color(0xff19547b),
        Color(0xffffd89b),
    )

    val clockColorGradient = listOf(
        Color(0xff780206),
        Color(0xff061161),
    )

    val width = (LocalConfiguration.current.screenWidthDp * 0.8)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(50.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(50)
                )
                .shadow(
                    elevation = 20.dp,
                    clip = true,
                    shape = RoundedCornerShape(50),
                    spotColor = MaterialTheme.colorScheme.primary,
                    ambientColor = MaterialTheme.colorScheme.primary
                )
                .padding(16.dp)
                .size(width.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawClockArc(colors = colorGradient)
                drawClockHands(
                    time = clock,
                    color = color,
                    background = background,
                    colors = clockColorGradient,
                    progress = clock.second * 6f,
                )
            }
        }
        CountDownWithNote(
            countdownTimer = timer,
        )
        RemainderPlayControl(currentTimerState = currentTimerState, onClick = onClick)
    }
}

private fun DrawScope.drawClockArc(colors: List<Color>) {
    // Draw the progress arc
    rotate(-90f) {
        drawArc(
            brush = Brush.linearGradient(
                colors = colors,
            ),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(100f)
        )
    }


}


private fun DrawScope.drawClockHands(
    progress: Float,
    time: Time,
    color: Color,
    background: Color,
    colors: List<Color>
) {
    val center = size.width / 2f
    val radius = size.width / 2f

    val hourAngle = Math.toRadians(((time.hour % 12) * 30.0 - 90.0) + (time.minute * 0.5))
    val minuteAngle = Math.toRadians(time.minute * 6.0 - 90.0)
    val secondAngle = Math.toRadians(time.second * 6.0 - 90.0)


    val gradient = Brush.linearGradient(
        colors = colors,
    )
    drawCircle(
        color = background,
        center = Offset(center, center),
        radius = radius
    )

    rotate(-90f) {
        drawArc(
            brush = gradient,
            startAngle = 0f,
            sweepAngle = progress,
            useCenter = true,
        )
    }
    drawLine(
        color = color,
        start = Offset(center, center),
        end = calculateHandEndPoint(center, radius * 0.5f, hourAngle),
        strokeWidth = 12f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(center, center),
        end = calculateHandEndPoint(center, radius * 0.7f, minuteAngle),
        strokeWidth = 8f,
        cap = StrokeCap.Round
    )

    drawLine(
        color = Color.Red,
        start = Offset(center, center),
        end = calculateHandEndPoint(center, radius * 0.9f, secondAngle),
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
}

private fun calculateHandEndPoint(center: Float, length: Float, angle: Double): Offset {
    val x = center + length * cos(angle).toFloat()
    val y = center + length * sin(angle).toFloat()
    return Offset(x, y)
}

@Preview(showBackground = false)
@Composable
fun ClockPreview() {
    BotTheme {
        Clock(
            clock = Time(
                hour = 4, minute = 30, second = 60, format = "AM", isFinished = false
            ),
            timer = Time(
                hour = 4, minute = 30, second = 60, format = "AM", isFinished = false
            )
        )
    }
}


fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
)