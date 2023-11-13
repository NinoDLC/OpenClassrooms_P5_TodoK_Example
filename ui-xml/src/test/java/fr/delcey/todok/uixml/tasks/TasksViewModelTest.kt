package fr.delcey.todok.uixml.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project_with_tasks.GetProjectsWithTasksUseCase
import fr.delcey.todok.domain.task.AddRandomTaskUseCase
import fr.delcey.todok.domain.task.DeleteTaskUseCase
import fr.delcey.todok.getDefaultProjectEntitySimple
import fr.delcey.todok.getDefaultProjectEntityWithTasksList
import fr.delcey.todok.getDefaultTaskEntity
import fr.delcey.todok.observeForTesting
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val deleteTaskUseCase: DeleteTaskUseCase = mockk()
    private val addRandomTaskUseCase: AddRandomTaskUseCase = mockk()
    private val getProjectsWithTasksUseCase: GetProjectsWithTasksUseCase = mockk()

    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setUp() {
        coJustRun { deleteTaskUseCase.invoke(any()) }
        coJustRun { addRandomTaskUseCase.invoke() }
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(getDefaultProjectEntityWithTasksList())

        tasksViewModel = TasksViewModel(
            deleteTaskUseCase = deleteTaskUseCase,
            addRandomTaskUseCase = addRandomTaskUseCase,
            getProjectsWithTasksUseCase = getProjectsWithTasksUseCase,
        )
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(getDefaultTasksViewStateItems(), it.value)
        }
    }

    @Test
    fun `edge case - sorted by project`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntitySimple(0).name),
                    getDefaultTasksViewStateItem(0, 0),
                    getDefaultTasksViewStateItem(0, 3),
                    getDefaultTasksViewStateItem(0, 6),
                    TasksViewStateItem.Header(title = getDefaultProjectEntitySimple(1).name),
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                    TasksViewStateItem.Header(title = getDefaultProjectEntitySimple(2).name),
                    getDefaultTasksViewStateItem(2, 2),
                    getDefaultTasksViewStateItem(2, 5),
                    getDefaultTasksViewStateItem(2, 8),
                ),
                it.value
            )
        }
    }

    @Test
    fun `edge case - after 2 clicks, back to sorted by TASK_CHRONOLOGICAL`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortButtonClicked()
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(getDefaultTasksViewStateItems(), it.value)
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(emptyList())

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(listOf(TasksViewStateItem.EmptyState), it.value)
        }
    }

    @Test
    fun `edge case - tasks available on one project only`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectEntityWithTasksList().map {
                if (it.id != 1L) {
                    it.copy(tasks = emptyList())
                } else {
                    it
                }
            }
        )

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(
                listOf(
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                ),
                it.value
            )
        }
    }

    @Test
    fun `edge case - tasks available on one project with PROJECT_ALPHABETICAL sorting`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectEntityWithTasksList().map {
                if (it.id != 1L) {
                    it.copy(tasks = emptyList())
                } else {
                    it
                }
            }
        )
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntitySimple(1).name),
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                ),
                it.value
            )
        }
    }

    @Test
    fun `edge case - only one project for tasks, sorted by project`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectEntityWithTasksList(projectCount = 1)
        )
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntitySimple(0).name),
                    getDefaultTasksViewStateItem(0, 0),
                    getDefaultTasksViewStateItem(0, 3),
                    getDefaultTasksViewStateItem(0, 6),
                ),
                it.value
            )
        }
    }

    @Test
    fun `verify onAddButtonClicked`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.onAddButtonClicked()

        // Then
        assertEquals(TasksEvent.NavigateToAddTask, tasksViewModel.singleLiveEvent.value)
    }

    @Test
    fun `verify onAddButtonLongClicked`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.onAddButtonLongClicked()
        runCurrent()

        // Then
        coVerify(exactly = 1) { addRandomTaskUseCase.invoke() }
        confirmVerified(addRandomTaskUseCase)
    }

    // region OUT
    private fun getDefaultTasksViewStateItems() = listOf(
        getDefaultTasksViewStateItem(0, 0),
        getDefaultTasksViewStateItem(1, 1),
        getDefaultTasksViewStateItem(2, 2),
        getDefaultTasksViewStateItem(0, 3),
        getDefaultTasksViewStateItem(1, 4),
        getDefaultTasksViewStateItem(2, 5),
        getDefaultTasksViewStateItem(0, 6),
        getDefaultTasksViewStateItem(1, 7),
        getDefaultTasksViewStateItem(2, 8),
    )

    private fun getDefaultTasksViewStateItem(projectId: Long, taskId: Long) = TasksViewStateItem.Task(
        taskId = getDefaultTaskEntity(projectId, taskId).id,
        projectColor = getDefaultProjectEntitySimple(projectId).colorInt,
        description = getDefaultTaskEntity(projectId, taskId).description,
    )
    // endregion OUT
}