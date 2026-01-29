package com.publicapp.takehome.data

import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.use_cases.ListSortingUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListSortingUseCaseTest {

    @Test
    fun `update lists for local tasks only`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
        )
        val remoteTasks = listOf<Task>()

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )
        assertTrue(localTasksToUpdate.isEmpty())
        assertEquals(1, remoteTasksToUpdate.size)
    }

    @Test
    fun `update lists for remote tasks only`() {
        val localTasks = listOf<Task>()
        val remoteTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            )
        )

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )
        assertEquals(1,localTasksToUpdate.size)
        assertTrue(remoteTasksToUpdate.isEmpty())
    }

    @Test
    fun `update lists one each for local and remote tasks`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            )
        )
        val remoteTasks = listOf<Task>(
            Task(
                id = "2",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            )
        )

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )
        assertEquals(1,localTasksToUpdate.size)
        assertEquals(1, remoteTasksToUpdate.size)
    }

    @Test
    fun `update lists for remote tasks only when local is newer`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            )
        )
        val remoteTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            )
        )

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )
        assertTrue(localTasksToUpdate.isEmpty())
        assertEquals(1, remoteTasksToUpdate.size)
    }

    @Test
    fun `update lists for local tasks only when remote is newer`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            )
        )
        val remoteTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            )
        )

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )

        assertEquals(1,localTasksToUpdate.size)
        assertTrue(remoteTasksToUpdate.isEmpty())
    }
}