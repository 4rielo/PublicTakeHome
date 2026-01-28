package com.ascarafia.publictakehome.ui.create_task

import androidx.appcompat.widget.DialogTitle

data class CreateTaskState(
    val isLoading: Boolean = false,
    val title: String = "",
    val description: String = "",
)