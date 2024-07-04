package fr.delcey.todok.uixml.add_task

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project.GetProjectsUseCase
import fr.delcey.todok.domain.task.AddTaskUseCase
import fr.delcey.todok.getDefaultProjectEntitySimple
import fr.delcey.todok.getDefaultProjectEntitySimpleList
import fr.delcey.todok.observeForTesting
import fr.delcey.todok.uixml.R
import fr.delcey.todok.uixml.utils.NativeText
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
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

    private val addTaskUseCase: AddTaskUseCase = mockk()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()

    private lateinit var addTaskViewModel: AddTaskViewModel

    @Before
    fun setUp() {
        coEvery { addTaskUseCase.invoke(any(), any()) } coAnswers {
            delay(INSERT_TASK_DELAY)
            true
        }
        every { getProjectsUseCase.invoke() } returns flowOf(getDefaultProjectEntitySimpleList())

        addTaskViewModel = AddTaskViewModel(
            addTaskUseCase = addTaskUseCase,
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
            getProjectsUseCase = getProjectsUseCase,
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(getDefaultAddTaskViewState(), it.value)
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
            assertEquals(
                getDefaultAddTaskViewState().copy(
                    isProgressBarVisible = true,
                    isOkButtonVisible = false,
                ),
                it.value
            )

            // When 2
            advanceTimeBy(INSERT_TASK_DELAY)
            runCurrent()

            // Then 2
            assertEquals(getDefaultAddTaskViewState(), it.value)
            assertEquals(AddTaskEvent.Dismiss, addTaskViewModel.singleLiveEvent.value)
            coVerify(exactly = 1) {
                addTaskUseCase.invoke(
                    projectId = DEFAULT_PROJECT_ID,
                    description = DEFAULT_DESCRIPTION,
                )
            }
            confirmVerified(addTaskUseCase)
        }
    }

    @Test
    fun `error case - can't insert task in database`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onTaskDescriptionChanged(DEFAULT_DESCRIPTION)
        addTaskViewModel.onOkButtonClicked()
        coEvery { addTaskUseCase.invoke(any(), any()) } returns false

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertEquals(getDefaultAddTaskViewState(), it.value)
            assertEquals(
                AddTaskEvent.Toast(NativeText.Resource(R.string.cant_insert_task)),
                addTaskViewModel.singleLiveEvent.value
            )
            coVerify(exactly = 1) {
                addTaskUseCase.invoke(
                    projectId = DEFAULT_PROJECT_ID,
                    description = DEFAULT_DESCRIPTION,
                )
            }
            confirmVerified(addTaskUseCase)
        }
    }

    @Test
    fun `edge case - user didn't specify projectId`() = testCoroutineRule.runTest {
        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertEquals(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task)),
            addTaskViewModel.singleLiveEvent.value
        )
        coVerify(exactly = 0) { addTaskUseCase.invoke(any(), any()) }
    }

    @Test
    fun `edge case - user didn't specify task description`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)

        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertEquals(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task)),
            addTaskViewModel.singleLiveEvent.value
        )
        coVerify(exactly = 0) { addTaskUseCase.invoke(any(), any()) }
    }

    @Test
    fun `edge case - user specified a blank task description`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onTaskDescriptionChanged("    ")

        // When
        addTaskViewModel.onOkButtonClicked()

        // Then
        assertEquals(
            AddTaskEvent.Toast(NativeText.Resource(R.string.error_inserting_task)),
            addTaskViewModel.singleLiveEvent.value
        )
        coVerify(exactly = 0) { addTaskUseCase.invoke(any(), any()) }
    }

    // region OUT
    private fun getDefaultAddTaskViewState() = AddTaskViewState(
        items = getDefaultAddTaskViewStateItems(),
        isProgressBarVisible = false,
        isOkButtonVisible = true,
    )

    private fun getDefaultAddTaskViewStateItems() = List(3) {
        AddTaskViewStateItem(
            projectId = getDefaultProjectEntitySimple(it).id,
            projectColor = getDefaultProjectEntitySimple(it).colorInt,
            projectName = getDefaultProjectEntitySimple(it).name,
        )
    }
    // endregion OUT
}