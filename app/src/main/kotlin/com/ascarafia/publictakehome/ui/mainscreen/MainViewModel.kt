package com.ascarafia.publictakehome.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            else -> {}
        }
    }

    private fun getRepositoryTasks() {
        viewModelScope.launch {
            taskRepository.getTasks().collect {
                _state.value = _state.value.copy(
                    tasks = it.sortedBy { it.isCompleted }
                )
            }
        }
    }
}