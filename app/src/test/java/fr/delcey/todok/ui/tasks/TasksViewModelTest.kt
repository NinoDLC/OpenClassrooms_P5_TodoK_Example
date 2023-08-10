package fr.delcey.todok.ui.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project_with_tasks.GetProjectsWithTasksUseCase
import fr.delcey.todok.domain.task.DeleteTaskUseCase
import fr.delcey.todok.domain.task.AddRandomTaskUseCase
import fr.delcey.todok.getDefaultProjectEntity
import fr.delcey.todok.getDefaultProjectWithTasksEntities
import fr.delcey.todok.getDefaultTaskEntity
import fr.delcey.todok.observeForTesting
import fr.delcey.todok.ui.utils.EquatableCallback
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
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
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(getDefaultProjectWithTasksEntities())

        tasksViewModel = TasksViewModel(
            deleteTaskUseCase = deleteTaskUseCase,
            addRandomTaskUseCase = addRandomTaskUseCase,
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            getProjectsWithTasksUseCase = getProjectsWithTasksUseCase,
        )
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTasksViewStateItems())
        }
    }

    @Test
    fun `nominal case - sorted by project`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntity(0).name),
                    getDefaultTasksViewStateItem(0, 0),
                    getDefaultTasksViewStateItem(0, 3),
                    getDefaultTasksViewStateItem(0, 6),
                    TasksViewStateItem.Header(title = getDefaultProjectEntity(1).name),
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                    TasksViewStateItem.Header(title = getDefaultProjectEntity(2).name),
                    getDefaultTasksViewStateItem(2, 2),
                    getDefaultTasksViewStateItem(2, 5),
                    getDefaultTasksViewStateItem(2, 8),
                )
            )
        }
    }

    @Test
    fun `nominal case - after 2 clicks, back to sorted by TASK_CHRONOLOGICAL`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortButtonClicked()
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTasksViewStateItems())
        }
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(emptyList())

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(listOf(TasksViewStateItem.EmptyState))
        }
    }

    @Test
    fun `edge case - tasks available on one project only`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectWithTasksEntities().map {
                if (it.projectEntity.id != 1L) {
                    it.copy(taskEntities = emptyList())
                } else {
                    it
                }
            }
        )

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                listOf(
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                )
            )
        }
    }

    @Test
    fun `edge case - tasks available on one project with PROJECT_ALPHABETICAL sorting`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectWithTasksEntities().map {
                if (it.projectEntity.id != 1L) {
                    it.copy(taskEntities = emptyList())
                } else {
                    it
                }
            }
        )
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntity(1).name),
                    getDefaultTasksViewStateItem(1, 1),
                    getDefaultTasksViewStateItem(1, 4),
                    getDefaultTasksViewStateItem(1, 7),
                )
            )
        }
    }

    @Test
    fun `edge case - only one project for tasks, sorted by project`() = testCoroutineRule.runTest {
        // Given
        every { getProjectsWithTasksUseCase.invoke() } returns flowOf(
            getDefaultProjectWithTasksEntities(projectCount = 1)
        )
        tasksViewModel.onSortButtonClicked()

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                listOf(
                    TasksViewStateItem.Header(title = getDefaultProjectEntity(0).name),
                    getDefaultTasksViewStateItem(0, 0),
                    getDefaultTasksViewStateItem(0, 1),
                    getDefaultTasksViewStateItem(0, 2),
                )
            )
        }
    }

    @Test
    fun `edge case - delete task`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {
            (it.value?.first() as TasksViewStateItem.Task).onDeleteEvent.invoke()
            runCurrent()

            // Then
            coVerify(exactly = 1) { deleteTaskUseCase.invoke(0) }
            confirmVerified(deleteTaskUseCase)
        }
    }

    @Test
    fun `edge case - delete task for third task`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {
            (it.value?.get(2) as TasksViewStateItem.Task).onDeleteEvent.invoke()
            runCurrent()

            // Then
            coVerify(exactly = 1) { deleteTaskUseCase.invoke(2) }
            confirmVerified(deleteTaskUseCase)
        }
    }

    @Test
    fun `verify onAddButtonClicked`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.onAddButtonClicked()

        // Then
        assertThat(tasksViewModel.singleLiveEvent.value).isEqualTo(TasksEvent.NavigateToAddTask)
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
        projectColor = getDefaultProjectEntity(projectId).colorInt,
        description = getDefaultTaskEntity(projectId, taskId).description,
        onDeleteEvent = EquatableCallback { }
    )
    // endregion OUT
}