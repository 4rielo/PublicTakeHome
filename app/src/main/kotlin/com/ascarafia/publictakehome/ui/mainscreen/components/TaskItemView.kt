package com.ascarafia.publictakehome.ui.mainscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ascarafia.publictakehome.domain.model.Task

@Composable
fun TaskItemView(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onTaskLongClick: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
        color = MaterialTheme.colorScheme.surface,
        onClick = { onTaskClick(task) },
        modifier = modifier
            .padding(5.dp)
            .alpha(if(task.isCompleted) 0.5f else 1f)
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
        modifier = Modifier
            .widthIn(max = 200.dp)
            .heightIn(max = 300.dp)
    )
}