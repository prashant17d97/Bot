package com.debugdesk.bot.presentation.screens.statics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun Statics(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Statics")
    }
}