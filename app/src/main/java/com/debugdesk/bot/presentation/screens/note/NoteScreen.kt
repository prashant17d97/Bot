package com.debugdesk.bot.presentation.screens.note

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.debugdesk.bot.R
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.emptyNote
import com.debugdesk.bot.navigation.noteId
import com.debugdesk.bot.presentation.reusablecompose.CustomAlertDialog
import org.koin.compose.koinInject

private const val TAG = "NoteScreen"

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    val noteViewModel: NoteViewModel = koinInject()

    var isEnabled by remember {
        mutableStateOf(false)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    val noteId = navHostController.currentBackStackEntry?.arguments?.getLong(noteId)
    val note by noteViewModel.note.collectAsState()


    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->

            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    noteId?.let { noteViewModel.retrieveNoteFromId(it) }
                }

                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> noteViewModel.clearNote()
                else -> {
                    Log.d(TAG, "NoteScreen: $event")
                }
            }

        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }

    }


    val colors by rememberUpdatedState(newValue = OutlinedTextFieldDefaults.colors()
        .takeIf { isEnabled } ?: OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedBorderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent
    ))
    val extraPadding by rememberUpdatedState(newValue = 10.dp.takeIf { isEnabled }
        ?: 0.dp)


    NoteEditor(
        modifier = modifier.padding(extraPadding),
        colors = colors,
        isEnabled = isEnabled,
        onEnabled = { isEnabled = it },
        note = note,
        onDelete = { showDialog = true },
        onUpdate = {
            showDialog = false
            isEnabled = false
            Log.e(TAG, "NoteScreen: $note")
            noteViewModel.updateNote(
                it.copy(
                    id = note.id,
                    createdAt = note.createdAt
                )
            )
        },
    )

    CustomAlertDialog(
        showDialog = showDialog,
        title = stringResource(id = R.string.alert),
        text = stringResource(id = R.string.note_delete_warning, note.heading),
        onConfirm = {
            showDialog = false
            isEnabled = false
            noteViewModel.deleteNote(note)
            navHostController.popBackStack()
        },
        onDismiss = {
            showDialog = false
        }
    )
}


@Composable
fun NoteEditor(
    modifier: Modifier = Modifier,
    note: Note,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    isEnabled: Boolean = false,
    onEnabled: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {},
    onUpdate: (Note) -> Unit = {},
) {

    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = isEnabled, block = {
        if (isEnabled) {
            title = note.heading
            description = note.description
        }
    })

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 10.dp, alignment = Alignment.Start
            )
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = note.heading.takeIf { !isEnabled } ?: title,
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                onValueChange = { title = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                maxLines = 1,
                enabled = isEnabled,
                shape = RoundedCornerShape(12.dp),
                colors = colors
            )

            AnimatedVisibility(visible = !isEnabled,
                enter = fadeIn() + slideInHorizontally { it },
                exit = fadeOut() + slideOutHorizontally { it }) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onEnabled(true) }
                )
            }

            Surface(
                onClick = onDelete,
                modifier = Modifier.size(35.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp),
                shadowElevation = 10.dp,
                tonalElevation = 10.dp,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(5.dp)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = note.description.takeIf { !isEnabled } ?: description,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default
            ),
            onValueChange = { description = it },
            enabled = isEnabled,
            shape = RoundedCornerShape(12.dp),
            colors = colors
        )

        AnimatedVisibility(visible = isEnabled,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.End
                )
            ) {
                Button(onClick = { onEnabled(false) }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
                Button(onClick = {
                    note.validateNewContent(
                        title = title,
                        description = description,
                        onUpdate = onUpdate,
                        onEnabled = onEnabled
                    )
                }) {
                    Text(
                        text = stringResource(id = R.string.update),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NoteViewPrev() {
    NoteEditor(modifier = Modifier.padding(10.dp), note = emptyNote)
}


fun Note.validateNewContent(
    title: String,
    description: String,
    onUpdate: (Note) -> Unit,
    onEnabled: (Boolean) -> Unit
) {
    if (validateNewContent(title, description)) {
        onUpdate(Note(heading = title, description = description))
    } else {
        onEnabled(false)
    }
}


fun Note.validateNewContent(
    title: String,
    description: String,
): Boolean {
    return when {
        this.heading == title.trim() && this.description == description.trim() -> false
        this.heading == title.trim() && description.isEmpty() -> false
        title.isEmpty() && this.description == description.trim() -> false
        else -> true
    }

}