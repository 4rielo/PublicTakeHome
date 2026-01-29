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
                onTaskClick(action.taskId)
            }

            is MainAction.OnTaskLongClick -> {
                if(_state.value.showTaskPopUp == null) {
                    toggleSelection(action.taskId)
                }
            }

            is MainAction.OnTaskCompletedToggle -> {
                viewModelScope.launch {
                    val updatedTask = action.task.copy(isCompleted = !action.task.isCompleted)
                    taskRepository.upsertTask(updatedTask)
                }
            }

            MainAction.OnCancelClick -> {
                _state.value = _state.value.copy(
                    selectedIds = emptyList(),
                    selectionMode = false
                )
            }

            MainAction.OnDeleteTasksClick -> {
                deleteSelectedTasks()
            }

            MainAction.OnUnPinTasksClick -> {
                unPinSelectedTasks()
            }

            MainAction.OnPinTasksClick -> {
                pinSelectedTasks()
            }

            else -> {}
        }
    }

    private fun onTaskClick(taskId: String?) {
        if(_state.value.selectionMode) {
            taskId?.let {
                val selectedTasks = _state.value.selectedIds as MutableList
                if(selectedTasks.contains(taskId)) {
                    selectedTasks.remove(taskId)
                } else {
                    selectedTasks.add(taskId)
                }

                _state.value = _state.value.copy(
                    selectedIds = selectedTasks,
                    selectionMode = true
                )
            }
        } else {
            val showTask: Task? = if (_state.value.showTaskPopUp != null) {
                null
            } else {
                _state.value.tasks.find { it.id == taskId }
            }
            _state.value = _state.value.copy(
                showTaskPopUp = showTask
            )
        }
    }

    private fun toggleSelection(taskId: String?) {
        if(_state.value.selectionMode) {
            _state.value = _state.value.copy(
                selectedIds = emptyList(),
                selectionMode = false,
            )
        } else {
            taskId?.let {
                _state.value = _state.value.copy(
                    selectedIds = _state.value.selectedIds + taskId,
                    selectionMode = true,
                )
            }
        }
    }

    private fun deleteSelectedTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            for(taskIds in _state.value.selectedIds) {
                taskRepository.deleteTask(taskIds)
            }

            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun pinSelectedTasks() {
        viewModelScope.launch {
            for (taskId in _state.value.selectedIds) {
                val unpinnedTask: Task? = _state.value.tasks.find { it.id == taskId }?.copy(isPinned = true)
                unpinnedTask?.let {
                    taskRepository.upsertTask(
                        unpinnedTask
                    )
                }
            }
        }
    }

    private fun unPinSelectedTasks() {
        viewModelScope.launch {

            for (taskId in _state.value.selectedIds) {
                val unpinnedTask: Task? = _state.value.tasks.find { it.id == taskId }?.copy(isPinned = false)
                unpinnedTask?.let {
                    taskRepository.upsertTask(
                        unpinnedTask
                    )
                }
            }
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