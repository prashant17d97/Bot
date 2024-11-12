package com.debugdesk.bot.utils.appstate

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.ui.graphics.vector.ImageVector
import com.debugdesk.bot.R

sealed class AtabActions(
    val visibility: Boolean = false,
    val tabIcon: ImageVector? = null,
    @StringRes val tabText: Int? = null,
    val tabIconContentDesc: String = "TabIcon",
) {

    data object NOTHING : AtabActions()

    data class Create(
        val tabVisibility: Boolean = true,
        val icon: ImageVector = Icons.Rounded.Create,
        @StringRes val text: Int = R.string.create,
        val iconDesc: String = "Create new Note"
    ) : AtabActions(
        visibility = tabVisibility,
        tabIcon = icon,
        tabText = text,
        tabIconContentDesc = iconDesc
    )

    data class Update(
        val tabVisibility: Boolean = true,
        val icon: ImageVector = Icons.Rounded.Create,
        @StringRes val text: Int? = null,
        val iconDesc: String = "Update Note"
    ) : AtabActions(
        visibility = tabVisibility,
        tabIcon = icon,
        tabText = text,
        tabIconContentDesc = iconDesc
    )

    data class Delete(
        val tabVisibility: Boolean = true,
        val icon: ImageVector = Icons.Rounded.Delete,
        @StringRes val text: Int? = null,
        val iconDesc: String = "Delete Note"
    ) : AtabActions(
        visibility = tabVisibility,
        tabIcon = icon,
        tabText = text,
        tabIconContentDesc = iconDesc
    )
}