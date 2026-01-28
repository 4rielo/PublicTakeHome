package com.ascarafia.publictakehome.ui.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.ascarafia.publictakehome.domain.model.DataError
import kotlinx.coroutines.launch

@Composable
fun SnackBarScreenHandler(
    snackbarHostState: SnackbarHostState
) {
    var snackBarErrorMessage: DataError? by remember { mutableStateOf(null) }
    var snackbarMessage: String? by remember { mutableStateOf(null) }

    SnackBarObserver(
        snackbarErrorMessage = snackBarErrorMessage,
        snackbarMessage = snackbarMessage,
        showSnackErrorBarMessage = {
            snackBarErrorMessage = it
        },
        showSnackBarMessage = {
            snackbarMessage = it
        },
        snackbarMessageShown = {
            snackBarErrorMessage = null
            snackbarMessage = null
        },
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun SnackBarObserver(
    snackbarErrorMessage: DataError? = null,
    snackbarMessage: String? = null,
    showSnackErrorBarMessage: (DataError) -> Unit,
    showSnackBarMessage: (String) -> Unit,
    snackbarMessageShown: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    ObserveAsEvents(
        SnackBarController.events
    ) { snackBarEvent ->
        snackbarHostState.currentSnackbarData?.dismiss()

        val result = snackbarHostState.showSnackbar(
            message = snackBarEvent.message,
            actionLabel = snackBarEvent.action?.label,
        )

        if (result == SnackbarResult.ActionPerformed) {
            snackBarEvent.action?.action?.invoke()
        }
    }

    ObserveAsEvents(
        SnackBarController.errorEvents
    ) {
        snackbarHostState.currentSnackbarData?.dismiss()
        showSnackErrorBarMessage(it)
    }

    if(snackbarErrorMessage != null) {
        val eventMessage = snackbarErrorMessage.getStringResource()
        showSnackBarMessage(eventMessage)
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                )
                snackbarMessageShown()
            }
        }
    }
}