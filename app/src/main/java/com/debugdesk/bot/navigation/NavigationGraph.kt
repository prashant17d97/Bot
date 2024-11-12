package com.debugdesk.bot.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.debugdesk.bot.presentation.screens.botapphome.BotAppHome
import com.debugdesk.bot.presentation.screens.note.NoteScreen
import com.debugdesk.bot.presentation.screens.remainderprofile.RemainderProfile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    pagerState: PagerState
) {
    NavHost(navController = navHostController, startDestination = Screens.BotAppHome.route) {
        composable(Screens.BotAppHome.route) {
            BotAppHome(
                navHostController = navHostController,
                pagerState = pagerState,
            )
        }
        composable(
            route = Screens.NoteView.route,
            arguments = listOf(
                navArgument(noteId) {
                    type = NavType.LongType
                }
            )
        ) {
            NoteScreen(navHostController = navHostController)
        }

        composable(Screens.RemainderProfile.route) {
            RemainderProfile(navHostController = navHostController)
        }

    }
}