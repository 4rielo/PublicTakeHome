package com.ascarafia.publictakehome.domain.model

sealed interface DataError: TaskAppError {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        UNKNOWN
    }

    enum class Local: DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN
    }
}