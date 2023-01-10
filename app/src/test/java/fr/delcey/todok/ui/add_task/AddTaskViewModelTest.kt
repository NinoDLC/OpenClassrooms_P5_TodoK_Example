package fr.delcey.todok.ui.add_task

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import fr.delcey.todok.R
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project.GetProjectsUseCase
import fr.delcey.todok.domain.task.InsertTaskUseCase
import fr.delcey.todok.domain.task.TaskEntity
import fr.delcey.todok.getDefaultProjectEntities
import fr.delcey.todok.getDefaultProjectEntity
import fr.delcey.todok.observeForTesting
import fr.delcey.todok.ui.utils.NativeText
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddTaskViewModelTest {

    companion object {
        private const val INSERT_TASK_DELAY = 50L

        private const val DEFAULT_PROJECT_ID = 1L
        private const val DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val insertTaskUseCase: InsertTaskUseCase = mockk()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()

    private val addTaskViewModel = AddTaskViewModel(
        insertTaskUseCase = insertTaskUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
        getProjectsUseCase = getProjectsUseCase,
    )

    @Before
    fun setUp() {
        coEvery { insertTaskUseCase.invoke(any()) } coAnswers {
            delay(INSERT_TASK_DELAY)
            true
        }
        every { getProjectsUseCase.invoke() } returns flowOf(getDefaultProjectEntities())
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultAddTaskViewState())
        }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onTaskDescriptionChanged(DEFAULT_DESCRIPTION)
        addTaskViewModel.onOkButtonClicked()

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                getDefaultAddTaskViewState().copy(
                    isProgressBarVisible = true,
                    isOkButtonVisible = false,
                )
            )

            // When 2
            advanceTimeBy(INSERT_TASK_DELAY)
            runCurrent()

            // Then 2
            assertThat(it.value).isEqualTo(getDefaultAddTaskViewState())
            assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(AddTaskEvent.Dismiss)
            coVerify(exactly = 1) {
                insertTaskUseCase.invoke(
                    TaskEntity(
                        id = 0,
                        projectId = DEFAULT_PROJECT_ID,
                        description = DEFAULT_DESCRIPTION
                    )
                )
            }
            confirmVerified(insertTaskUseCase)
        }
    }

    @Test
    fun `error case - can't insert task in database`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onTaskDescriptionChanged(DEFAULT_DESCRIPTION)
        addTaskViewModel.onOkButtonClicked()
        coEvery { insertTaskUseCase.invoke(any()) } returns false

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultAddTaskViewState())
            assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(
                AddTaskEvent.Toast(NativeText.Resource(R.string.cant_insert_task))
            )
            coVerify(exactly = 1) {
                insertTaskUseCase.invoke(
                    TaskEntity(
                        id = 0,
                        projectId = DEFAULT_PROJECT_ID,
                        description = DEFAULT_DESCRIPTION
                    )
                )
            }
            confirmVerified(insertTaskUseCase)
        }
    }

    @Test
    fun `edge case - user didn't specify projectId`() = testCoroutineRule.runTest {
        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task))
        )
        coVerify(exactly = 0) { insertTaskUseCase.invoke(any()) }
    }

    @Test
    fun `edge case - user didn't specify task description`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)

        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task))
        )
        coVerify(exactly = 0) { insertTaskUseCase.invoke(any()) }
    }

    @Test
    fun `edge case - user specified a blank task description`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onTaskDescriptionChanged("    ")

        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task))
        )
        coVerify(exactly = 0) { insertTaskUseCase.invoke(any()) }
    }

    // region OUT
    private fun getDefaultAddTaskViewState() = AddTaskViewState(
        items = getDefaultAddTaskViewStateItems(),
        isProgressBarVisible = false,
        isOkButtonVisible = true,
    )

    private fun getDefaultAddTaskViewStateItems() = List(3) {
        AddTaskViewStateItem(
            projectId = getDefaultProjectEntity(it).id,
            projectColor = getDefaultProjectEntity(it).colorInt,
            projectName = getDefaultProjectEntity(it).name,
        )
    }
    // endregion OUT
}