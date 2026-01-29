package com.ascarafia.publictakehome.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("id") val id: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("completed") val isCompleted: Boolean? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("is_pinned") val isPinned: Boolean? = null,
    @SerialName("is_deleted") val isDeleted: Boolean? = null,
    @SerialName("last_updated") val lastUpdated: String? = null
)
