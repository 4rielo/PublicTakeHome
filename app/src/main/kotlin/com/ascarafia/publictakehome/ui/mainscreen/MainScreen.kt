package com.ascarafia.publictakehome.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.ui.mainscreen.components.TaskItemView
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme

@Composable
fun MainRoot(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
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
        )

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
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2)
        ) {
            items(state.tasks, { it.id } ) {
                TaskItemView(
                    task = it,
                    onTaskClick = {},
                    onTaskLongClick = {},
                    modifier = Modifier
                        .sizeIn(maxWidth = 200.dp, maxHeight = 300.dp)
                )
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