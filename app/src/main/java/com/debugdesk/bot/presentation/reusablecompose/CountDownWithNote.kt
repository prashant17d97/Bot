package com.debugdesk.bot.presentation.reusablecompose

import android.opengl.Visibility
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.presentation.screens.tracker.TrackerViewModel
import com.debugdesk.bot.utils.timeutil.Time
import com.debugdesk.bot.utils.timeutil.getCurrentTime
import org.koin.compose.koinInject

@Composable
fun CountDownWithNote(
    modifier: Modifier = Modifier,
    countdownTimer: Time,
    visibility: Boolean=false,
    notes: List<Note> = emptyList(),
    reverseFlip: Boolean = false,
    trackerViewModel: TrackerViewModel = koinInject(),
    onFabClick: () -> Unit = {},
    onNoteClick: (Note) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
            space = 10.dp, alignment = Alignment.Top
        )
    ) {
        CountDownTimerContainer(
            modifier = Modifier,
            countdownTimer = countdownTimer,
            reverseFlip = reverseFlip,
        )
        NoteItems(
            notes = notes,
            onNoteClick = onNoteClick,
            trackerViewModel = trackerViewModel,
        )
        AnimatedVisibility(
            visible = visibility,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(onClick = onFabClick) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Fab"
                    )
                }
            }
        }

    }
}


@Composable
private fun NoteItems(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit = {},
    trackerViewModel: TrackerViewModel,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        items(notes) {
            Note(
                note = it,
                onNoteClick = onNoteClick,
                trackerViewModel = trackerViewModel,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Note(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteClick: (Note) -> Unit = {},
    trackerViewModel: TrackerViewModel
) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(true) }
    val dismissState = rememberDismissState(confirmValueChange = {
        if (it == DismissValue.DismissedToStart) {
            visible = false
            trackerViewModel.deleteNote(note)
            Toast.makeText(
                context, "${note.heading} has been removed!", Toast.LENGTH_SHORT
            ).show()
            true
        } else false
    }, positionalThreshold = { 180.dp.toPx() })

    AnimatedVisibility(
        visible = visible, exit = fadeOut(spring())
    ) {
        SwipeToDismiss(state = dismissState,
            modifier = modifier,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                DismissContent(
                    modifier = modifier, note = note, onNoteClick = onNoteClick
                )
            })
    }

}

@Composable
fun DismissContent(
    modifier: Modifier = Modifier, note: Note, onNoteClick: (Note) -> Unit
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .clickable { onNoteClick(note) }
        .padding(vertical = 5.dp, horizontal = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 10.dp,
            focusedElevation = 10.dp,
            hoveredElevation = 10.dp,
            draggedElevation = 10.dp,
            disabledElevation = 10.dp,
        )) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.heading,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = note.date,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
            }
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection
    val color by animateColorAsState(
        targetValue = when (dismissState.dismissDirection) {
            DismissDirection.EndToStart -> Color(0xFFFF1744)
            DismissDirection.StartToEnd -> Color.Transparent//Color(0xFF1DE9B6)
            null -> Color.Transparent
        }, label = ""
    )
    val alignment = Alignment.CenterEnd
    val icon = Icons.Default.Delete

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f, label = ""
    )


    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .background(
                color = color, shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (direction == DismissDirection.StartToEnd) Icon(
            Icons.Default.Delete, contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) Icon(
            icon,
            contentDescription = "Delete Icon",
            modifier = Modifier
                .scale(scale)
                .padding(end = 10.dp)
        )
    }
}


@Composable
private fun CountDownTimerContainer(
    modifier: Modifier = Modifier, countdownTimer: Time, reverseFlip: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CountDownValueContainer(
            modifier = Modifier.weight(1f), value = countdownTimer.hour, reverseFlip = reverseFlip
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        CountDownValueContainer(
            modifier = Modifier.weight(1f), value = countdownTimer.minute, reverseFlip = reverseFlip
        )
        Text(
            text = ":",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        CountDownValueContainer(
            modifier = Modifier.weight(1f), value = countdownTimer.second, reverseFlip = reverseFlip
        )

        if (countdownTimer.format.isNotEmpty()) {
            SpacerWidth(value = 20)
            CountDownFormatContainer(
                modifier = Modifier.weight(1f), value = countdownTimer.format,
//                borderWidth = 2,
//                borderColor = Color.Transparent,
                reverseFlip = reverseFlip
            )
        }

    }
}


@Composable
fun CountDownValueContainer(
    modifier: Modifier = Modifier,
    itemHeight: Int = 80,
    value: Int,
    borderWidth: Int = 2,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    reverseFlip: Boolean = false
) {
    // Scrollable content with vertical animation
    Box(
        modifier = modifier
            .height((itemHeight).dp)
            .border(
                width = borderWidth.dp, color = borderColor, shape = RoundedCornerShape(20.dp)
            ), contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = value, label = "Animate content", transitionSpec = {
            fadeIn() togetherWith slideOutVertically {
                if (reverseFlip) {
                    it
                } else {
                    -it
                }
            }
        }) {
            Text(
                text = String.format("%02d", it),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
            )
        }
    }
}


@Composable
private fun CountDownFormatContainer(
    modifier: Modifier = Modifier,
    itemHeight: Int = 80,
    value: String,
    borderWidth: Int = 2,
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    reverseFlip: Boolean = false
) {
    // Scrollable content with vertical animation
    Box(
        modifier = modifier
            .height((itemHeight).dp)
            .border(
                width = borderWidth.dp, color = borderColor, shape = RoundedCornerShape(20.dp)
            ), contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = value, label = "Animate content", transitionSpec = {
            fadeIn() togetherWith slideOutVertically {
                if (reverseFlip) {
                    it
                } else {
                    -it
                }
            }
        }) {
            Text(
                text = it,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderPreview() = CountDownWithNote(
    countdownTimer = getCurrentTime(),
    notes = listOf(
        Note(heading = "Note", description = "Note", createdAt = "2024-01-28T14:10:24Z"),
        Note(heading = "Note", description = "Note", createdAt = "2024-01-28T14:10:24Z"),
    ),
)

@Preview
@Composable
fun DismissContentPre() {
    DismissContent(note = Note(
        heading = "Note", description = "Note", createdAt = "2024-01-28T14:10:24Z"
    ), onNoteClick = {})
}