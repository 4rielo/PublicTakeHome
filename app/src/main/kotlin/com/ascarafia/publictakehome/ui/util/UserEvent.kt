package com.ascarafia.publictakehome.ui.util

import com.ascarafia.publictakehome.domain.model.TaskAppError

sealed interface UserEvent {

    data class DataError(val error: TaskAppError): UserEvent
    data class Error(val errorMessage: String): UserEvent
    object GoBack: UserEvent
    object Success: UserEvent
}