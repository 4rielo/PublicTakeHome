package com.ascarafia.publictakehome.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationIndex {

    @Serializable
    object Home : NavigationIndex

    @Serializable
    object CreateTask : NavigationIndex
}