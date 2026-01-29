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
): Comparable<Task> {
    override fun compareTo(other: Task): Int {
        return id.compareTo(other.id)
    }

    init {
        require(title.length <= 50) {
            "Title must be at most 50 characters"
        }
        require(description.length <= 200) {
            "Description must be at most 200 characters"
        }
    }
}
