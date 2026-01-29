package com.ascarafia.publictakehome.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.ui.main.components.MultiSelectionBanner
import com.ascarafia.publictakehome.ui.main.components.SearchBar
import com.ascarafia.publictakehome.ui.main.components.TaskItemView
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme
import com.ascarafia.publictakehome.ui.util.DeviceConfiguration
import com.ascarafia.publictakehome.ui.util.custom_animation.shakeRotation
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import com.publicapp.takehome.R

@Composable
fun MainRoot(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
    onAction: (MainAction) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
    ) {
        MainScreen(
            state = state,
            onAction = onAction,
            modifier = modifier
                .clickable(
                    enabled = state.showTaskPopUp != null,
                    onClick = {
                        onAction(MainAction.OnTaskClick(null))
                    }
                )
                .blur(if (state.showTaskPopUp != null) 10.dp else 0.dp)
        )

        AnimatedVisibility (
            state.showTaskPopUp != null,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            state.showTaskPopUp?.let { task ->
                TaskItemView(
                    task = task,
                    onTaskClick = {},
                    onTaskLongClick = {},
                    enableClick = false,
                    showTaskCompletedToggle = true,
                    onTaskCompletedToggle = { onAction(MainAction.OnTaskCompletedToggle(it)) },
                    onEditTaskClicked = { onAction(MainAction.OnEditTaskClick(it.id)) },
                    modifier = Modifier
                        .padding(50.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
                onAction(MainAction.HideCreateTask(true))
            } ?: onAction(MainAction.HideCreateTask(false))
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val layout = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val staggeredColumns = when(layout) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 2
        DeviceConfiguration.MOBILE_LANDSCAPE -> 3
        DeviceConfiguration.TABLET_PORTRAIT -> 4
        DeviceConfiguration.TABLET_LANDSCAPE -> 6
        DeviceConfiguration.DESKTOP -> 8
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
        AnimatedVisibility(!state.selectionMode) {
            SearchBar(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                enabled = state.showTaskPopUp == null,
                searchQuery = state.searchQuery,
                onSearchQueryChange = { onAction(MainAction.OnSearchQueryChange(it)) },
                onImeSearch = { onAction(MainAction.OnSearchQueryChange(it)) }
            )
        }

        AnimatedVisibility(state.selectionMode) {
            MultiSelectionBanner(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                onUnPinTasksClicked = { onAction(MainAction.OnUnPinTasksClick) },
                onPinTasksClicked = { onAction(MainAction.OnPinTasksClick) },
                onDeleteTaskClicked = { onAction(MainAction.OnDeleteTasksClick) },
                onCancelClicked = { onAction(MainAction.OnCancelClick) }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            val verticalScrollState = rememberLazyStaggeredGridState()
            val showGoBackUpButton by remember {
                derivedStateOf {
                    verticalScrollState.firstVisibleItemIndex > 0
                }
            }

            LaunchedEffect(showGoBackUpButton, state.selectionMode) {
                if (state.showTaskPopUp == null) {
                    onAction(MainAction.HideCreateTask(showGoBackUpButton || state.selectionMode))
                }
            }

            val rotation = shakeRotation(state.selectionMode)

            val pinnedTasks = state.tasks.filter { it.isPinned }
            val unpinnedTasks = state.tasks.filter { !it.isPinned }

            LazyVerticalStaggeredGrid(
                state = verticalScrollState,
                columns = StaggeredGridCells.Fixed(staggeredColumns)
            ) {
                if(pinnedTasks.isNotEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                stringResource(R.string.task_list_pinned_tasks),
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                            HorizontalDivider(thickness = 2.dp)
                        }
                    }

                    items(pinnedTasks, { it.id }) { task ->
                        val isSelected = state.selectedIds.contains(task.id)
                        TaskItemView(
                            task = task,
                            onTaskClick = {
                                onAction(MainAction.OnTaskClick(task.id))
                            },
                            onTaskLongClick = {
                                onAction(MainAction.OnTaskLongClick(task.id))
                            },
                            isSelected = isSelected,
                            modifier = Modifier
                                .sizeIn(maxWidth = 200.dp, maxHeight = 300.dp)
                                .graphicsLayer {
                                    rotationZ = rotation
                                }
                        )
                    }

                    item(span = StaggeredGridItemSpan.FullLine) {
                        Spacer(
                            modifier =
                                Modifier
                                    .height(15.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                stringResource(R.string.task_list_other_tasks),
                                modifier = Modifier
                                    .padding(10.dp)
                            )

                            HorizontalDivider(thickness = 2.dp)
                        }
                    }
                }

                items(unpinnedTasks, { it.id }) { task ->
                    val isSelected = state.selectedIds.contains(task.id)
                    TaskItemView(
                        task = task,
                        onTaskClick = {
                            onAction(MainAction.OnTaskClick(task.id))
                        },
                        onTaskLongClick = {
                            onAction(MainAction.OnTaskLongClick(task.id))
                        },
                        isSelected = isSelected,
                        modifier = Modifier
                            .sizeIn(maxWidth = 200.dp, maxHeight = 300.dp)
                            .graphicsLayer {
                                rotationZ = rotation
                            }
                    )
                }
            }

            val lifecycleScope = rememberCoroutineScope()
            this@Column.AnimatedVisibility(
                visible = showGoBackUpButton,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = {
                        lifecycleScope.launch {
                            verticalScrollState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "go back up",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val mainState = MainState(
        tasks = listOf(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                isCompleted = false,
                createdAt = "2023-01-01",
                lastUpdated = ""
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2, but this is waaaaay bigger, because it takes up to a few rows.",
                isCompleted = false,
                createdAt = "2023-01-01",
                lastUpdated = ""
            ),
            Task(
                id = "3",
                title = "Task 3",
                description = "Description 3, and yes, this is slightly larger than the first one.",
                isCompleted = false,
                createdAt = "2023-01-01",
                lastUpdated = ""
            ),
        )
    )

    PublicTakeHomeTheme {
        MainScreen(
            state = mainState,
            onAction = {}
        )
    }
}