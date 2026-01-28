package com.ascarafia.publictakehome.ui.main

import com.ascarafia.publictakehome.domain.model.Task

sealed interface MainAction {
    data class HideCreateTask(val hide: Boolean): MainAction
    data class OnTaskClick(val taskId: String?): MainAction
    data class OnTaskLongClick(val taskId: String): MainAction
    data class OnTaskCompletedToggle(val task: Task): MainAction
    data class OnEditTaskClick(val taskId: String): MainAction

}