package com.debugdesk.bot.utils.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object Paddings {

    fun PaddingValues.padding(
        start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp, bottom: Dp = 0.dp
    ): PaddingValues {
        val topPadding = calculateTopPadding()
        val bottomPadding = calculateBottomPadding()
        val rightPadding = calculateRightPadding(LayoutDirection.Rtl)
        val leftPadding = calculateLeftPadding(LayoutDirection.Ltr)
        return PaddingValues(
            start = (start.value + rightPadding.value).dp,
            top = (top.value + topPadding.value).dp,
            end = (end.value + leftPadding.value).dp,
            bottom = (bottom.value + bottomPadding.value).dp
        )
    }

    fun PaddingValues.padding(
        horizontal: Dp = 0.dp,
        vertical: Dp = 0.dp,
    ): PaddingValues {
        val topPadding = calculateTopPadding()
        val bottomPadding = calculateBottomPadding()
        val rightPadding = calculateRightPadding(LayoutDirection.Rtl)
        val leftPadding = calculateLeftPadding(LayoutDirection.Ltr)
        return PaddingValues(
            start = (vertical.value + rightPadding.value).dp,
            top = (horizontal.value + topPadding.value).dp,
            end = (vertical.value + leftPadding.value).dp,
            bottom = (horizontal.value + bottomPadding.value).dp
        )
    }

    fun PaddingValues.padding(
        all: Dp = 0.dp,
    ): PaddingValues {
        val topPadding = calculateTopPadding()
        val bottomPadding = calculateBottomPadding()
        val rightPadding = calculateRightPadding(LayoutDirection.Rtl)
        val leftPadding = calculateLeftPadding(LayoutDirection.Ltr)
        return PaddingValues(
            start = (all.value + rightPadding.value).dp,
            top = (all.value + topPadding.value).dp,
            end = (all.value + leftPadding.value).dp,
            bottom = (all.value + bottomPadding.value).dp
        )
    }
}