package com.ascarafia.publictakehome.ui.util

import com.ascarafia.publictakehome.domain.model.DataError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SnackBarEvent(
    val message: String,
    val action: SnackbarAction? = null
)

data class SnackbarAction(
    val label: String,
    val action: () -> Unit
)

object SnackBarController {
    private val _events = Channel<SnackBarEvent>()
    val events = _events.receiveAsFlow()

    private val _errorEvents = Channel<DataError>()
    val errorEvents = _errorEvents.receiveAsFlow()

    suspend fun show(event: SnackBarEvent) {
        _events.send(event)
    }

    suspend fun showErrorSnackbar(errorEvent: DataError) {
        _errorEvents.send(errorEvent)
    }
}