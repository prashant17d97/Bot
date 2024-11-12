package com.debugdesk.bot

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.debugdesk.bot.presentation.screens.botapp.BotApp
import com.debugdesk.bot.presentation.screens.botapp.BotAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private var isTrue = true

    private val botAppViewModel: BotAppViewModel by inject()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        botAppViewModel.init()
        installSplashScreen().setKeepOnScreenCondition {
            isTrue
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        enableEdgeToEdge()

        lifecycleScope.launch {
            delay(1000)
            isTrue = false
        }


        setContent {
            BotApp()
        }
    }
}

