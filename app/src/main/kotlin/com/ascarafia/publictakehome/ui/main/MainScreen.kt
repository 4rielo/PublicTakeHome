package com.ascarafia.publictakehome.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.transition.Transition
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.ui.main.components.TaskItemView
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
                .blur(if(state.showTaskPopUp != null) 10.dp else 0.dp)
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
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

            LaunchedEffect(showGoBackUpButton) {
                onAction(MainAction.HideCreateTask(showGoBackUpButton))
            }

            LazyVerticalStaggeredGrid(
                state = verticalScrollState,
                columns = StaggeredGridCells.Fixed(2)
            ) {
                items(state.tasks, { it.id }) { task ->
                    TaskItemView(
                        task = task,
                        onTaskClick = {
                            onAction(MainAction.OnTaskClick(task.id))
                        },
                        onTaskLongClick = {},
                        modifier = Modifier
                            .sizeIn(maxWidth = 200.dp, maxHeight = 300.dp)
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
            ),
            Task(
                id = "2",
                title = "Task 2",
                description = "Description 2, but this is waaaaay bigger, because it takes up to a few rows.",
                isCompleted = false,
                createdAt = "2023-01-01",
            ),
            Task(
                id = "3",
                title = "Task 3",
                description = "Description 3, and yes, this is slightly larger than the first one.",
                isCompleted = false,
                createdAt = "2023-01-01",
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