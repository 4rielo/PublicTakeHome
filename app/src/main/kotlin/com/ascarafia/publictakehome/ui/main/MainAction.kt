package com.ascarafia.publictakehome.ui.main

sealed interface MainAction {
    data class HideCreateTask(val hide: Boolean): MainAction
    data class OnTaskClick(val taskId: String?): MainAction
    data class OnTaskLongClick(val taskId: String): MainAction
}