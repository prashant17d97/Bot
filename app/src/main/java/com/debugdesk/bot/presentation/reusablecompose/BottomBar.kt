package com.debugdesk.bot.presentation.reusablecompose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.utils.enums.BottomBar


@Composable
fun BottomBarWithNavigation(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    currentTabIndex: Int,
    bottomBarTabs: List<BottomBar>,
    onClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        AnimatedVisibility(visible = visibility,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            BottomBar(
                currentTabIndex = currentTabIndex,
                bottomBarTabs = bottomBarTabs,
                onClick = onClick
            )
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentTabIndex: Int,
    bottomBarTabs: List<BottomBar>,
    onClick: (Int) -> Unit
) {

    BottomNavigation {
        // Iterate through each tab and compose BottomNavigationItem for it
        bottomBarTabs.forEachIndexed { index, screen ->
            BottomNavigationColumnScopeItem(
                modifier = modifier.weight(1f),
                selected = currentTabIndex == index,
                onClick = { onClick.invoke(index) },
            ) { size, tabContentColor ->
                // Icon composable for each tab
                Icon(
                    painter = painterResource(screen.iconId),
                    contentDescription = stringResource(id = screen.stringId),
                    modifier = Modifier.size(size),
                    tint = tabContentColor,
                )
                Text(text = stringResource(id = screen.stringId), color = tabContentColor)
            }
        }
    }

}


/**
 * Composable function representing a generic Bottom Navigation.
 * It includes a [Surface] with a [Row] layout for the icons.
 *
 * @param modifier Modifier for customization.
 * @param backgroundColor Color of the background.
 * @param contentColor Color of the content.
 * @param elevation Elevation of the surface.
 * @param content RowScope lambda for content composition.
 */
@Composable
private fun BottomNavigation(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 8.dp,
    content: @Composable RowScope.() -> Unit,
) {
    // Surface composable for the bottom navigation
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        tonalElevation = elevation,
        modifier = modifier,
    ) {
        // Row composable for the icons
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}


/**
 * Composable function representing a Bottom Navigation Item.
 * It includes dynamic animations for icon size and color based on selection state.
 *
 * @param modifier Modifier for customization.
 * @param iconSize Size of the icon.
 * @param activeColor Color when the item is selected.
 * @param inactiveColor Color when the item is not selected.
 * @param selected Whether the item is currently selected.
 * @param enabled Whether the item is enabled.
 * @param interactionSource MutableInteractionSource for interaction handling.
 * @param onClick Lambda for click handling.
 * @param content Lambda for content composition with icon size and color parameters.
 */
@Composable
private fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    iconSize: Int = 24,
    activeColor: Color = MaterialTheme.colorScheme.primaryContainer,
    inactiveColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selected: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable (Dp, Color) -> Unit,
) {
    // Animated state for dynamic icon size
    val contentIconSize by animateDpAsState(
        targetValue =
        if (selected) {
            (iconSize * 1.35).dp
        } else {
            iconSize.dp
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "contentIconSize",
    )

    // Animated state for dynamic tab color
    val tabColor by animateColorAsState(
        targetValue =
        if (selected) {
            activeColor
        } else {
            inactiveColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "tabColor",
    )

    // Animated state for dynamic tab content color
    val tabContentColor by animateColorAsState(
        targetValue =
        if (selected) {
            inactiveColor
        } else {
            activeColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "tabContentColor",
    )

    // Box composable for the bottom navigation item
    Box(
        modifier =
        modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, color = LocalContentColor.current),
            )
            .background(tabColor),
        contentAlignment = Alignment.Center,
    ) {
        // Compose the content with dynamic icon size and color
        content(contentIconSize, tabContentColor)
    }
}


/**
 * Composable function representing a Bottom Navigation Item.
 * It includes dynamic animations for icon size and color based on selection state.
 *
 * @param modifier Modifier for customization.
 * @param iconSize Size of the icon.
 * @param activeColor Color when the item is selected.
 * @param inactiveColor Color when the item is not selected.
 * @param selected Whether the item is currently selected.
 * @param enabled Whether the item is enabled.
 * @param interactionSource MutableInteractionSource for interaction handling.
 * @param onClick Lambda for click handling.
 * @param content Lambda for content composition with icon size and color parameters.
 */
@Composable
private fun BottomNavigationColumnScopeItem(
    modifier: Modifier = Modifier,
    iconSize: Int = 24,
    activeColor: Color = MaterialTheme.colorScheme.primaryContainer,
    inactiveColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    selected: Boolean,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable ColumnScope.(Dp, Color) -> Unit,
) {
    // Animated state for dynamic icon size
    val contentIconSize by animateDpAsState(
        targetValue =
        if (selected) {
            (iconSize * 1.35).dp
        } else {
            iconSize.dp
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "contentIconSize",
    )

    // Animated state for dynamic tab color
    val tabColor by animateColorAsState(
        targetValue =
        if (selected) {
            activeColor
        } else {
            inactiveColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "tabColor",
    )

    // Animated state for dynamic tab content color
    val tabContentColor by animateColorAsState(
        targetValue =
        if (selected) {
            inactiveColor
        } else {
            activeColor
        },
        animationSpec =
        TweenSpec(
            durationMillis = 300,
            easing = FastOutSlowInEasing,
        ),
        label = "tabContentColor",
    )

    // Box composable for the bottom navigation item
    Column(
        modifier =
        modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, color = LocalContentColor.current),
            )
            .background(tabColor),
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Compose the content with dynamic icon size and color
        content(contentIconSize, tabContentColor)
    }
}