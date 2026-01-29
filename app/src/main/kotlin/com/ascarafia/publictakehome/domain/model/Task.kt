package com.ascarafia.publictakehome.domain.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdAt: String,
    val isPinned: Boolean = false
) {
    init {
        require(title.length <= 50) {
            "Title must be at most 50 characters"
        }
        require(description.length <= 200) {
            "Description must be at most 200 characters"
        }
    }
}
