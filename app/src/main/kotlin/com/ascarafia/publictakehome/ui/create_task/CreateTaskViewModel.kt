package com.ascarafia.publictakehome.ui.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import com.ascarafia.publictakehome.ui.navigation.NavigationIndex
import com.ascarafia.publictakehome.ui.util.UserEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class CreateTaskViewModel(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    val taskId = savedStateHandle.toRoute<NavigationIndex.CreateTask>().taskId

    private val _screenActions = Channel<UserEvent>()
    val screenActions = _screenActions.receiveAsFlow()

    private val _state = MutableStateFlow(CreateTaskState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                taskId?.let {
                    val task = taskRepository.getTask(it)
                    _state.value = _state.value.copy(
                        title = task?.title ?: "",
                        description = task?.description ?: ""
                    )
                }
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateTaskState()
        )

    fun onAction(action: CreateTaskAction) {
        when (action) {
            is CreateTaskAction.OnTitleChanged -> {
                _state.value = _state.value.copy(
                    title = action.title.take(50)
                )
            }
            is CreateTaskAction.OnDescriptionChanged -> {
                _state.value = _state.value.copy(
                    description = action.description.take(200)
                )
            }
            is CreateTaskAction.OnSaveClicked -> {
                viewModelScope.launch {
                    val task = taskId?.let {
                        taskRepository.getTask(taskId)
                    }
                    val upsertResponse = taskRepository.upsertTask(
                        task?.copy(
                            title = _state.value.title,
                            description = _state.value.description
                        ) ?: Task(
                            id = UUID.randomUUID().toString(),
                            title = _state.value.title,
                            description = _state.value.description,
                            createdAt = LocalDateTime.now().toString()
                        )
                    )
                    when(upsertResponse) {
                        is Result.Success -> {
                            _screenActions.send(UserEvent.GoBack)
                        }
                        is Result.Error -> {
                            _screenActions.send(UserEvent.DataError(upsertResponse.error))
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}