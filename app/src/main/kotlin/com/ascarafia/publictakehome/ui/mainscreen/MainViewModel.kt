package com.ascarafia.publictakehome.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ascarafia.publictakehome.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                _state.value = _state.value.copy(
                    tasks = getFakeTasks()
                )

                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    fun onAction(action: MainAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

    private fun getFakeTasks() = listOf(
        Task(
            id = "1",
            title = "Task 1",
            description = "Description 1",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
        Task(
            id = "2",
            title = "Task 2",
            description = "Description 2, but this is waaaaay bigger, because it takes up to a few rows.",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
        Task(
            id = "3",
            title = "Task 3",
            description = "Description 3, and yes, this is slightly larger than the first one.",
            isCompleted = false,
            createdAt = "2023-01-01",
        ),
    )
}