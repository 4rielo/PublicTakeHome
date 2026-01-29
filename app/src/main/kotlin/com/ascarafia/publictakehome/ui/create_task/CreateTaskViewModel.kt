package com.ascarafia.publictakehome.ui.create_task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ascarafia.publictakehome.domain.model.DataError
import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.model.Result
import com.ascarafia.publictakehome.domain.repositories.TaskRepository
import com.ascarafia.publictakehome.ui.navigation.NavigationIndex
import com.ascarafia.publictakehome.ui.util.UserEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random
import kotlin.time.Clock

class CreateTaskViewModel(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    val taskId = savedStateHandle.toRoute<NavigationIndex.CreateTask>().taskId

    private val _screenEvents = Channel<UserEvent>()
    val screenEvents = _screenEvents.receiveAsFlow()

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
                    val simulatedDelay = Random.nextLong(500, 2500)

                    _state.value = _state.value.copy(
                        isLoading = true
                    )

                    delay(simulatedDelay)

                    val success = Random.nextInt(0,4)

                    if(success == 0) {
                        val randomError = Random.nextInt(0,9)
                        val error = when (randomError) {
                            0 -> DataError.Local.DISK_FULL
                            1 -> DataError.Remote.SERVER
                            2 -> DataError.Remote.NOT_FOUND
                            3 -> DataError.Remote.UNAUTHORIZED
                            4 -> DataError.Remote.REQUEST_TIMEOUT
                            5 -> DataError.Remote.TOO_MANY_REQUESTS
                            6 -> DataError.Remote.NO_INTERNET
                            7 -> DataError.Remote.SERIALIZATION
                            else -> DataError.Remote.UNKNOWN
                        }
                        _screenEvents.trySend(UserEvent.DataError(error))
                    } else {
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
                                createdAt = Clock.System.now().toString()
                            )
                        )
                        when(upsertResponse) {
                            is Result.Success -> {
                                _screenEvents.send(UserEvent.GoBack)
                            }
                            is Result.Error -> {
                                _screenEvents.send(UserEvent.DataError(upsertResponse.error))
                            }
                        }
                    }

                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                }
            }
            else -> Unit
        }
    }
}