package com.ascarafia.publictakehome.ui.main.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ascarafia.publictakehome.domain.model.Task
import com.publicapp.takehome.R as Res

@Composable
fun TaskItemView(
    modifier: Modifier = Modifier,
    task: Task,
    enableClick: Boolean = true,
    onTaskClick: (Task) -> Unit,
    onTaskLongClick: (Task) -> Unit,
    showTaskCompletedToggle: Boolean = false,
    onTaskCompletedToggle: (Task) -> Unit = {},
    onEditTaskClicked: (Task) -> Unit = {},
    onDeleteTaskClicked: (Task) -> Unit = {},
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
        color = MaterialTheme.colorScheme.surface,
        onClick = { onTaskClick(task) },
        enabled = enableClick,
        modifier = modifier
            .padding(5.dp)
            .alpha(if (task.isCompleted && !showTaskCompletedToggle) 0.5f else 1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(task.title)

            Spacer(modifier = Modifier.height(10.dp))

            Text(task.description)

            if(showTaskCompletedToggle) {
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            onEditTaskClicked(task)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit task",
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onTaskCompletedToggle(task) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskItemViewPreview() {
    TaskItemView(
        task = Task(
            id = "1",
            title = "Prueba",
            description = "Contenido de la tarea",
            isCompleted = false,
            createdAt = "",
            isPinned = false
        ),
        onTaskClick = {},
        onTaskLongClick = {},
        showTaskCompletedToggle = true,
        modifier = Modifier
            .widthIn(max = 200.dp)
            .heightIn(max = 300.dp)
    )
}