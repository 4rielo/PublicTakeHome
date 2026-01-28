package com.ascarafia.publictakehome.ui.main

import com.ascarafia.publictakehome.domain.model.Task

data class MainState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
)