package com.ascarafia.publictakehome.ui.create_task

sealed interface CreateTaskAction {
    data class OnTitleChanged(val title: String): CreateTaskAction
    data class OnDescriptionChanged(val description: String): CreateTaskAction
    object OnSaveClicked: CreateTaskAction
    object OnDiscardClicked: CreateTaskAction
}