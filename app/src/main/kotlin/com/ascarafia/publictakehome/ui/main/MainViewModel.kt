package com.ascarafia.publictakehome.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val tasksList: StateFlow<List<Task>> = taskRepository
        .getTasks()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            emptyList()
        )

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                _state.value = _state.value.copy(isLoading = true)
                getRepositoryTasks()
                hasLoadedInitialData = true
                _state.value = _state.value.copy(isLoading = false)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private var tasksFilterJob: Job? = null

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

            is MainAction.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = action.query)
                filterTasks(action.query)
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
            tasksList.collect { tasks ->
                val showTaskPopUp = tasks.find { it.id == _state.value.showTaskPopUp?.id }
                _state.value = _state.value.copy(
                    tasks = tasks.sortedBy { it.isCompleted },
                    showTaskPopUp = showTaskPopUp
                )
            }
        }
    }

    private fun filterTasks(query: String) {
        tasksFilterJob?.cancel()
        tasksFilterJob = viewModelScope.launch {
            delay(500)
            with(Dispatchers.Default) {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
                val result = tasksList.value
                    .filter { task ->
                        task.title.contains(query, ignoreCase = true) || task.description.contains(query, ignoreCase = true)
                    }
                    .sortedBy { it.isCompleted }
                _state.update {
                    it.copy(
                        tasks = result,
                        isLoading = false
                    )
                }
            }
        }
    }
}