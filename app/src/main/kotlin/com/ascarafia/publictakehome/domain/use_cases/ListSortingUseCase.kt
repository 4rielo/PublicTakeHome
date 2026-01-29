package com.ascarafia.publictakehome.domain.use_cases

import com.ascarafia.publictakehome.domain.model.Task
import kotlin.time.Clock
import kotlin.time.Instant

object ListSortingUseCase {

    enum class AddToList {
        LOCAL,
        LOCAL_UPDATING_TIME,
        REMOTE,
        NONE
    }
    private fun addToListBasedOnLastUpdated(localTask: Task, remoteTask: Task): AddToList {
        val localInstant = DateTimeUtils.fromIsoString(localTask.lastUpdated)
        val remoteInstant = DateTimeUtils.fromIsoString(remoteTask.lastUpdated)

        return remoteInstant?.let {
            val lastUpdatedDifference = (localInstant?.compareTo(remoteInstant) ?: -1)
            when {
                lastUpdatedDifference == 0 -> AddToList.NONE
                lastUpdatedDifference > 0 -> AddToList.REMOTE
                else -> AddToList.LOCAL
            }
        } ?: AddToList.LOCAL_UPDATING_TIME
    }

    fun getTasksToUpdate(localTasksList: List<Task>, remoteTasksList: List<Task>): Pair<List<Task>, List<Task>> {
        val remoteTasksToUpdate = mutableSetOf<Task>()
        val localTasksToUpdate = mutableSetOf<Task>()

        for (localTask in localTasksList) {
            val matchingRemoteTask = remoteTasksList.firstOrNull { it.id == localTask.id }
            matchingRemoteTask?.let {
                when(
                    addToListBasedOnLastUpdated(localTask, matchingRemoteTask)
                ) {
                    AddToList.LOCAL -> localTasksToUpdate.add(matchingRemoteTask)
                    AddToList.REMOTE -> remoteTasksToUpdate.add(matchingRemoteTask)
                    AddToList.LOCAL_UPDATING_TIME -> {
                        val localLastUpdated = DateTimeUtils.toIsoString(Clock.System.now())
                        localTasksToUpdate.add( matchingRemoteTask.copy(lastUpdated = localLastUpdated ) )
                    }
                    AddToList.NONE -> Unit
                }

            } ?: run {
                remoteTasksToUpdate.add(localTask)
            }
        }

        for (remoteTask in remoteTasksList) {
            val matchingLocalTask = localTasksList.firstOrNull { it.id == remoteTask.id }
            matchingLocalTask?.let {
                when(
                    addToListBasedOnLastUpdated(matchingLocalTask, remoteTask)
                ) {
                    AddToList.LOCAL -> localTasksToUpdate.add(matchingLocalTask)
                    AddToList.REMOTE -> remoteTasksToUpdate.add(matchingLocalTask)
                    AddToList.LOCAL_UPDATING_TIME -> {
                        val localLastUpdated = DateTimeUtils.toIsoString(Clock.System.now())
                        localTasksToUpdate.add( matchingLocalTask.copy(lastUpdated = localLastUpdated ) )
                    }
                    AddToList.NONE -> Unit
                }

            } ?: run {
                localTasksToUpdate.add(remoteTask)
            }
        }

        return Pair( localTasksToUpdate.toList(), remoteTasksToUpdate.toList())
    }
}