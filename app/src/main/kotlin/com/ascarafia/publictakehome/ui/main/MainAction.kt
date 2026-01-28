package com.ascarafia.publictakehome.ui.main

sealed interface MainAction {
    data class HideCreateTask(val hide: Boolean): MainAction
}