package com.ascarafia.publictakehome.ui.create_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ascarafia.publictakehome.ui.theme.PublicTakeHomeTheme
import org.koin.compose.viewmodel.koinViewModel
import com.publicapp.takehome.R

@Composable
fun CreateTaskRoot(
    modifier: Modifier = Modifier,
    viewModel: CreateTaskViewModel = koinViewModel(),
    onAction: (CreateTaskAction) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
    ) {
        CreateTaskScreen(
            state = state,
            onAction = onAction,
            modifier = modifier
                .blur(if(state.isLoading) 10.dp else 0.dp)
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
fun CreateTaskScreen(
    state: CreateTaskState,
    onAction: (CreateTaskAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = { onAction(CreateTaskAction.OnTitleChanged(it)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(15.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = { onAction(CreateTaskAction.OnDescriptionChanged(it)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onAction(CreateTaskAction.OnSaveClicked)
                }
            ),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .heightIn(min = 15.dp)
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.primaryContainer
                ),
                onClick = {
                    onAction(CreateTaskAction.OnDiscardClicked)
                }
            ) {
                Text(
                    stringResource(R.string.create_new_task_cancel),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            OutlinedButton (
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    onAction(CreateTaskAction.OnSaveClicked)
                },
            ) {
                Text(
                stringResource(R.string.create_new_task_save),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PublicTakeHomeTheme {
        CreateTaskScreen(
            state = CreateTaskState(),
            onAction = {}
        )
    }
}