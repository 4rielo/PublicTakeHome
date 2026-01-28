package com.ascarafia.publictakehome.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                getRepositoryTasks()
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
            is MainAction.OnTaskClick -> {
                val showTask: Task? = if(_state.value.showTaskPopUp != null) {
                    null
                } else {
                    _state.value.tasks.find { it.id == action.taskId }
                }
                _state.value = _state.value.copy(
                    showTaskPopUp = showTask
                )
            }
            is MainAction.OnTaskCompletedToggle -> {
                viewModelScope.launch {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskRepository.upsertTask(updatedTask)
                }
            }
            else -> {}
        }
    }

    private fun getRepositoryTasks() {
        viewModelScope.launch {
            taskRepository.getTasks().collect { tasks ->
                val showTaskPopUp = tasks.find { it.id == _state.value.showTaskPopUp?.id }
                _state.value = _state.value.copy(
                    tasks = tasks.sortedBy { it.isCompleted },
                    showTaskPopUp = showTaskPopUp
                )
            }
        }
    }
}