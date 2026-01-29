package com.ascarafia.publictakehome.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ascarafia.publictakehome.domain.model.DataError
import com.publicapp.takehome.R

@Composable
fun DataError.getStringResource(): String {
    val eventMessageId = when(this) {
        DataError.Local.DISK_FULL -> R.string.disk_full
        DataError.Remote.REQUEST_TIMEOUT -> R.string.request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> R.string.too_many_requests
        DataError.Remote.NO_INTERNET -> R.string.no_internet
        DataError.Remote.SERVER -> R.string.server
        DataError.Remote.SERIALIZATION -> R.string.serialization
        DataError.Remote.UNAUTHORIZED -> R.string.unauthorized
        DataError.Remote.NOT_FOUND -> R.string.not_found
        DataError.Remote.UNKNOWN, DataError.Local.UNKNOWN -> R.string.unknown
        else -> R.string.unknown
    }

    return stringResource(eventMessageId)
}