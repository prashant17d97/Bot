package com.debugdesk.bot.presentation.screens.botapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.debugdesk.bot.R
import com.debugdesk.bot.navigation.NavigationGraph
import com.debugdesk.bot.navigation.Screens
import com.debugdesk.bot.presentation.reusablecompose.BottomBarWithNavigation
import com.debugdesk.bot.presentation.reusablecompose.UpDownDragGesture
import com.debugdesk.bot.ui.theme.BotTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BotApp() {
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 1) {
        3
    }

    val navHostController: NavHostController = rememberNavController()
    val botAppViewModel: BotAppViewModel = koinInject()

    val isScrollGuideShown by botAppViewModel.isScrollGuideShown.collectAsState()

    LaunchedEffect(isScrollGuideShown) {
        delay(2000)
        botAppViewModel.scrollGuideShown()
    }

    val currentRoute by navHostController.currentBackStackEntryAsState()

    BotTheme {
        BotScaffold(
            modifier = Modifier,
            navHostController = navHostController,
            botAppViewModel = botAppViewModel,
            currentRoute = currentRoute,
            pagerState = pagerState,
            coroutine = coroutine
        )

        UpDownDragGesture(
            isVisible = pagerState.currentPage == 1 && !isScrollGuideShown,
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BotScaffold(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    botAppViewModel: BotAppViewModel,
    currentRoute: NavBackStackEntry?,
    pagerState: PagerState,
    coroutine: CoroutineScope,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBarContainer(
                navHostController = navHostController
            )
        },
        bottomBar = {
            BottomBarWithNavigation(modifier = Modifier,
                visibility = currentRoute?.destination?.route == Screens.BotAppHome.route,
                currentTabIndex = pagerState.currentPage,
                bottomBarTabs = botAppViewModel.bottomTab,
                onClick = { index ->
                    coroutine.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })

        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(
                navHostController = navHostController, pagerState = pagerState
            )
        }
    }
}

@Composable
private fun TopAppBarContainer(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val currentRoute by navHostController.currentBackStackEntryAsState()
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(top = 30.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                10.dp, alignment = Alignment.Start
            )
        ) {
            AnimatedVisibility(
                visible = currentRoute?.destination?.route != Screens.BotAppHome.route,
                enter = fadeIn() + slideInHorizontally { it },
                exit = fadeOut() + slideOutHorizontally { it },
            ) {
                Image(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "AppLogo",
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(30)
                        )
                        .shadow(
                            elevation = 20.dp,
                            clip = true,
                            shape = RoundedCornerShape(30),
                            spotColor = Color.Black,
                            ambientColor = Color.Black
                        )
                        .padding(5.dp)
                        .clickable {
                            navHostController.popBackStack()
                        })
            }

            AppTopBar()
        }
    }
}

@Composable
fun AppTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 5.dp, alignment = Alignment.Start
        )
    ) {
        Box(modifier = Modifier.size(50.dp)) {
            Image(
                painter = painterResource(id = R.drawable.splash), contentDescription = "AppLogo"
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(
                space = 2.dp, alignment = Alignment.Top
            ), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(id = R.string.slogan),
                style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic)
            )
        }
    }
}


@Preview
@Composable
fun AppTopPrev() {
    TopAppBarContainer(
        navHostController = rememberNavController(),
    )
}