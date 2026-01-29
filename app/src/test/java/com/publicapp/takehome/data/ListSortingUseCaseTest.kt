package com.publicapp.takehome.data

import com.ascarafia.publictakehome.domain.model.Task
import com.ascarafia.publictakehome.domain.use_cases.ListSortingUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListSortingUseCaseTest {

    @Test
    fun `update lists for remote tasks only`() {
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
    fun `update lists for local tasks only`() {
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
    fun `update lists on remote tasks only, when local is newer`() {
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
    fun `update lists on local tasks only, when remote is newer`() {
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

    @Test
    fun `update lists when remote is newer, but local contains an extra task`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "2",
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
        assertEquals(1,remoteTasksToUpdate.size)
    }

    @Test
    fun `update lists when local is newer, but remote contains an extra task`() {
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
            ),
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
        assertEquals(1,remoteTasksToUpdate.size)
    }

    @Test
    fun `update lists when local is newer, but remote contains extra tasks, large list sets`() {
        val localTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "2",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "3",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "4",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "5",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "6",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
            Task(
                id = "7",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-02T00:00:00Z"
            ),
        )
        val remoteTasks = listOf<Task>(
            Task(
                id = "1",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "2",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "3",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "4",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "5",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "6",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),Task(
                id = "7",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "8",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),Task(
                id = "9",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "10",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "11",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "12",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "13",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "14",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "15",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "16",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
            Task(
                id = "17",
                title = "Task 1",
                description = "Description 1",
                createdAt = "2023-08-01T00:00:00Z",
                lastUpdated = "2023-08-01T00:00:00Z"
            ),
        )

        val (localTasksToUpdate, remoteTasksToUpdate) = ListSortingUseCase
            .getTasksToUpdate(
                localTasks,
                remoteTasks
            )

        assertEquals(10,localTasksToUpdate.size)
        assertEquals(7,remoteTasksToUpdate.size)
    }
}