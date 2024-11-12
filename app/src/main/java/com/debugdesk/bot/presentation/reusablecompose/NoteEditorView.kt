package com.debugdesk.bot.presentation.reusablecompose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.R
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.enums.NoteEditorButton
import com.debugdesk.bot.presentation.screens.tracker.TrackerViewModel
import com.debugdesk.bot.ui.theme.BotTheme
import com.debugdesk.bot.utils.extensions.CommonFunctions.trim
import org.koin.compose.koinInject

@Composable
fun NoteEditorView(
    modifier: Modifier = Modifier,
    note: Note,
    positiveButton: NoteEditorButton = NoteEditorButton.SAVE,
    onNoteChange: (Note) -> Unit,
    trackerViewModel: TrackerViewModel = koinInject(),
    onNegativeClick: () -> Unit = {}
) {
    val context = LocalContext.current

    NoteEditor(
        modifier = modifier,
        note = note,
        positiveButton = positiveButton,
        onNoteChange = onNoteChange,
        onPositiveClick = {
            val (validate, message) = when {
                note.heading.isEmpty() -> false to context.getString(R.string.title_empty)
                note.description.isEmpty() -> false to context.getString(R.string.note_empty)
                else -> true to ""
            }
            if (validate) {
                when (positiveButton) {
                    NoteEditorButton.UPDATE -> {
                        trackerViewModel.updateNote(
                            note.trim()
                        )
                    }

                    NoteEditorButton.SAVE, NoteEditorButton.CREATE -> {
                        trackerViewModel.createNote(note.trim())
                    }
                }
                onNegativeClick()
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

        },
        onNegativeClick = onNegativeClick
    )
}

@Composable
fun NoteEditor(
    modifier: Modifier = Modifier,
    note: Note,
    positiveButton: NoteEditorButton = NoteEditorButton.SAVE,
    onNoteChange: (Note) -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.End)
        ) {
            Button(onClick = { onNegativeClick() }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Button(onClick = { onPositiveClick() }) {
                Text(
                    text = stringResource(id = positiveButton.stringId),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = note.heading,
            textStyle = MaterialTheme.typography.titleLarge,
            placeholder = { Text(text = stringResource(id = R.string.title)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            onValueChange = { onNoteChange(note.copy(heading = it)) },
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            value = note.description,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            textStyle = MaterialTheme.typography.titleSmall,
            placeholder = { Text(text = stringResource(id = R.string.note)) },
            onValueChange = { onNoteChange(note.copy(description = it)) },
        )

    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun NoteEditorPrev() {
    var note: Note by remember {
        mutableStateOf(Note(heading = "", description = ""))
    }
    BotTheme {
        NoteEditorView(
            modifier = Modifier.fillMaxSize(),
            note = note,
            onNoteChange = { note = it },
        )
    }
}

