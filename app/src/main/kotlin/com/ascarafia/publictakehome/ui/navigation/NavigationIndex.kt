package com.ascarafia.publictakehome.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationIndex {

    @Serializable
    object Home : NavigationIndex

    @Serializable
    data class CreateTask(val taskId: String? = null) : NavigationIndex
}