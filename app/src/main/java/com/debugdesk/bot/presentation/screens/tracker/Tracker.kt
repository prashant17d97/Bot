package com.debugdesk.bot.presentation.screens.tracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.debugdesk.bot.datamodel.Note
import com.debugdesk.bot.datamodel.emptyNote
import com.debugdesk.bot.enums.NoteEditorButton
import com.debugdesk.bot.navigation.Screens
import com.debugdesk.bot.presentation.reusablecompose.Clock
import com.debugdesk.bot.presentation.reusablecompose.CountDownWithNote
import com.debugdesk.bot.presentation.reusablecompose.InitiateCountdown
import com.debugdesk.bot.presentation.reusablecompose.NoteEditorView
import com.debugdesk.bot.utils.timeutil.Time
import com.debugdesk.bot.utils.timeutil.TimerState
import com.debugdesk.bot.utils.timeutil.getCurrentTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.time.Duration.Companion.seconds

@Composable
fun Tracker(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {

    val trackerViewModel: TrackerViewModel = koinInject()
    var trackerModel by remember {
        mutableStateOf(
            TrackerModel()
        )
    }

    val notes: List<Note> by trackerViewModel.notes.collectAsState()
    val countdownTimer: Time by trackerViewModel.time.collectAsState()
    val timerState: TimerState by trackerViewModel.timerState.collectAsState()

    LaunchedEffect(Unit) {
        while (true) {
            trackerModel = trackerModel.copy(clock = getCurrentTime())
            delay(1.seconds)
        }
    }

    LaunchedEffect(notes) {
        trackerViewModel.getAllNotes()
    }
    LaunchedEffect(notes, countdownTimer, timerState) {
        trackerModel = trackerModel.copy(
            notes = notes,
            countdownTimer = countdownTimer,
            timerState = timerState
        )
    }

    TrackerContainer(
        modifier = modifier,
        trackerModel = trackerModel,
        trackerViewModel = trackerViewModel,
        navHostController = navHostController,
        onTrackerModelChange = {
            trackerModel = it
        }
    )

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackerContainer(
    modifier: Modifier = Modifier,
    trackerModel: TrackerModel,
    navHostController: NavHostController,
    trackerViewModel: TrackerViewModel,
    onTrackerModelChange: (TrackerModel) -> Unit
) {
    val pagerState = rememberPagerState {
        2
    }
    val scope = rememberCoroutineScope()
    val tabs = listOf("Clock", "Notes")
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.Top
        )
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage, modifier = Modifier) {
            tabs.forEachIndexed { index, text ->
                Tab(
                    selected = pagerState.currentPage == index,
                    modifier = Modifier.height(40.dp),
                    onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                    content = {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    })
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 64.dp),
            verticalAlignment = Alignment.Top
        ) {
            when (it) {
                1 -> CountDownWithNote(
                    countdownTimer = trackerModel.countdownTimer,
                    notes = trackerModel.notes,
                    trackerViewModel = trackerViewModel,
                    visibility = true,
                    onFabClick = {
                        onTrackerModelChange(
                            trackerModel.copy(
                                showBSM = true,
                                showDigitalTimer = true
                            )
                        )
                    },
                    onNoteClick = { noteForUpdate ->
                        navHostController.navigate(Screens.NoteView.passNoteId(noteForUpdate.id))
                    }
                )

                0 -> Clock(
                    clock = trackerModel.clock,
                    timer = trackerModel.countdownTimer,
                    currentTimerState = trackerModel.timerState,
                    onClick = { countdownTimer ->
                        onTrackerModelChange(trackerModel.copy(timerState = countdownTimer))
                        if (countdownTimer == TimerState.Started) {
                            onTrackerModelChange(
                                trackerModel.copy(
                                    showBSM = true,
                                    showDigitalTimer = false
                                )
                            )
                        } else {
                            trackerViewModel.handleTimerAndState(
                                timerState = countdownTimer,
                                timerParameter = trackerViewModel.timerParameter
                            )
                        }
                    }
                )
            }
        }
    }

    BottomSheet(
        showBSM = trackerModel.showBSM,
        onBSMChange = { onTrackerModelChange(trackerModel.copy(showBSM = false)) },
        content = {
            if (trackerModel.showDigitalTimer) {
                NoteEditorView(
                    note = trackerModel.note,
                    positiveButton = trackerModel.positiveButton,
                    onNoteChange = { onTrackerModelChange(trackerModel.copy(note = it)) },
                    trackerViewModel = trackerViewModel,
                    onNegativeClick = {
                        onTrackerModelChange(
                            trackerModel.copy(
                                showBSM = false,
                                note = emptyNote,
                                positiveButton = NoteEditorButton.SAVE
                            )
                        )
                    },
                )
            } else {
                InitiateCountdown(
                    onStart = {
                        onTrackerModelChange(
                            trackerModel.copy(
                                showBSM = false,
                                timerState = TimerState.Started
                            )
                        )
                        trackerViewModel.timerParameter = it
                        trackerViewModel.handleTimerAndState(TimerState.Started, it)
                    },
                    onCancel = { onTrackerModelChange(trackerModel.copy(showBSM = false)) })
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    showBSM: Boolean = false,
    onBSMChange: () -> Unit,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(showBSM) {
        if (!showBSM) {
            sheetState.hide()
        }
    }


    AnimatedVisibility(showBSM) {
        ModalBottomSheet(
            modifier = modifier.height(600.dp), onDismissRequest = {
                onBSMChange()
            }, sheetState = sheetState, content = content
        )
    }
}

@Preview
@Composable
private fun TrackerPrev() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInHorizontally { it },
                exit = fadeOut() + slideOutHorizontally { it }) {
                FloatingActionButton(onClick = {
//                    onTrackerModelChange(trackerModel.copy(showBSM = true))
                }) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Fab"
                    )
                }
            }
        }
    }

}