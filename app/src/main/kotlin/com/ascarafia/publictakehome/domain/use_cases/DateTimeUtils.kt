package com.ascarafia.publictakehome.domain.use_cases

import kotlin.time.Instant

object DateTimeUtils {
    fun fromIsoString(dateString: String): Instant? {
        return try {
            return Instant.parse(dateString )
        } catch (e: Exception) {
            null
        }
    }

    fun toIsoString(instant: Instant): String {
        return instant.toString()
    }
}