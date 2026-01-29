package com.ascarafia.publictakehome.domain.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val isPinned: Boolean = false,
    val isDeleted: Boolean = false,
    val lastUpdated: String,
) {
    override fun equals(other: Any?): Boolean =
        other is Task && other.id == id

    override fun hashCode(): Int = id.hashCode()

    init {
        require(title.length <= 50) {
            "Title must be at most 50 characters"
        }
        require(description.length <= 200) {
            "Description must be at most 200 characters"
        }
    }
}
