package com.debugdesk.bot.presentation.screens.botapphome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.debugdesk.bot.presentation.screens.tracker.Tracker
import com.debugdesk.bot.presentation.screens.settings.Settings
import com.debugdesk.bot.presentation.screens.statics.Statics

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BotAppHome(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier.padding(10.dp),
        userScrollEnabled = false,
        verticalAlignment = Alignment.Top
    ) {
        when (it) {
            0 -> Statics(navHostController = navHostController)
            1 -> Tracker(navHostController = navHostController)
            2 -> Settings(navHostController = navHostController)
            else -> {}
        }
    }
}

