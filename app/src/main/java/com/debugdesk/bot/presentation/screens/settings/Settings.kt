package com.debugdesk.bot.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.debugdesk.bot.R
import com.debugdesk.bot.navigation.Screens
import com.debugdesk.bot.presentation.reusablecompose.CustomAlertDialog
import com.debugdesk.bot.utils.enums.Theme
import org.koin.compose.koinInject

@Composable
fun Settings(modifier: Modifier = Modifier, navHostController: NavHostController) {

    val settingViewModel: SettingViewModel = koinInject()
    val isDynamicColor by settingViewModel.isDynamicColor.collectAsState()
    val theme by settingViewModel.theme.collectAsState()

    val showThemeDropDown by settingViewModel.showThemeDropDown.collectAsState()
    val showDialog by settingViewModel.showResetDialog.collectAsState()
    MenuContainer(
        modifier = modifier,
        navHostController = navHostController,
        settingViewModel = settingViewModel,
        isDynamicColor = isDynamicColor,
        theme = theme
    )

    DropDownMenu(
        isExpanded = showThemeDropDown,
        selectedTheme = theme,
        onDismiss = { settingViewModel.updateTheme(theme) },
        onItemClickEvent = { selectedTheme ->
            settingViewModel.updateTheme(selectedTheme)
        }
    )

    CustomAlertDialog(
        showDialog = showDialog,
        title = stringResource(id = R.string.info),
        dismissButtonText = stringResource(id = R.string.no),
        text = stringResource(id = R.string.reset_warning),
        onConfirm = {
            settingViewModel.reset()
        },
        onDismiss = {
            settingViewModel.updateResetDialog(false)
        },

        )
}


@Composable
fun MenuContainer(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    settingViewModel: SettingViewModel,
    theme: Theme,
    isDynamicColor: Boolean
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        content = {
            items(settingViewModel.settingMenus) {
                SettingMenuItem(
                    currentTheme = stringResource(id = theme.stringId),
                    settingClicksEvent = it,
                    isDynamicColor = isDynamicColor,
                    onClicksEvent = { settingClicksEvent ->
                        settingViewModel.handleMenuClick(settingClicksEvent, navHostController)
                    }
                )
            }
        }
    )
}

@Composable
fun SettingMenuItem(
    modifier: Modifier = Modifier,
    settingClicksEvent: SettingClicksEvent,
    currentTheme: String,
    isDynamicColor: Boolean,
    onClicksEvent: (SettingClicksEvent) -> Unit
) {
    if (settingClicksEvent.isVisible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        if (settingClicksEvent !is SettingClicksEvent.UseDynamicColor) {
                            onClicksEvent(settingClicksEvent)
                        }
                    }
                    .height(60.dp)
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = settingClicksEvent.menuText),
                    style = MaterialTheme.typography.titleMedium
                )
                when (settingClicksEvent) {
                    SettingClicksEvent.Theme -> ThemeTrailing(currentTheme)
                    is SettingClicksEvent.UseDynamicColor -> DynamicColorSwitch(
                        isUsingDynamicColor = isDynamicColor,
                        onClicksEvent = onClicksEvent
                    )

                    else -> Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, "")
                }
            }
        }
    }

}


@Composable
fun ThemeTrailing(
    currentTheme: String = stringResource(id = R.string.dark),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = currentTheme, style = MaterialTheme.typography.titleMedium)
        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, "")
    }
}


@Composable
fun DynamicColorSwitch(isUsingDynamicColor: Boolean, onClicksEvent: (SettingClicksEvent) -> Unit) {
    Switch(
        checked = isUsingDynamicColor,
        onCheckedChange = { onClicksEvent(SettingClicksEvent.UseDynamicColor(!isUsingDynamicColor)) })
}

@Preview
@Composable
fun SettingCardPrev() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top)
    ) {
        SettingMenuItem(
            modifier = Modifier,
            settingClicksEvent = SettingClicksEvent.UseDynamicColor(true),
            currentTheme = "Dark",
            isDynamicColor = true
        ) {

        }
        SettingMenuItem(
            modifier = Modifier,
            settingClicksEvent = SettingClicksEvent.Theme,
            currentTheme = "Dark",
            isDynamicColor = true
        ) {

        }
        SettingMenuItem(
            modifier = Modifier,
            settingClicksEvent = SettingClicksEvent.CreateRemainderProfile(Screens.BotAppHome.route),
            currentTheme = "Dark",
            isDynamicColor = true
        ) {

        }

        DropDownMenu(isExpanded = true, selectedTheme = Theme.Dark, onItemClickEvent = {})
    }
}


/**
 * Private Composable function representing a dropdown menu.
 *
 * @param dropdownItem The list of items for the dropdown menu.
 * @param isExpanded Flag indicating whether the dropdown menu is expanded.
 * @param selectedTheme The currently selected app theme.
 * @param onDismiss The callback to be invoked when the dropdown menu is dismissed.
 * @param onItemClickEvent The callback to be invoked when an item in the dropdown menu is clicked.
 */
@Composable
private fun DropDownMenu(
    isExpanded: Boolean,
    selectedTheme: Theme,
    onDismiss: () -> Unit = {},
    onItemClickEvent: (Theme) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(12.dp), color = Color.Transparent)
            .wrapContentSize(Alignment.TopEnd),
    ) {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = onDismiss,
        ) {
            Theme.entries.forEach { appTheme ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = appTheme.stringId),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    onClick = { onItemClickEvent(appTheme) },
                    trailingIcon = {
                        AnimatedVisibility(visible = selectedTheme == appTheme) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "CurrentTheme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    })
            }

        }

    }
}

