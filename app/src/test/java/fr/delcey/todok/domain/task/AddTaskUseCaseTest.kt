package fr.delcey.todok.domain.task

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.getDefaultTaskEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddTaskUseCaseTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 3L
        private const val DEFAULT_TASK_ID = 7L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskRepository: TaskRepository = mockk()

    private val addTaskUseCase = AddTaskUseCase(taskRepository)

    @Before
    fun setUp() {
        coEvery { taskRepository.add(any()) } returns true
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = addTaskUseCase.invoke(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID))

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { taskRepository.add(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID)) }
        confirmVerified(taskRepository)
    }

    @Test
    fun `error case`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskRepository.add(any()) } returns false

        // When
        val result = addTaskUseCase.invoke(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID))

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskRepository.add(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID)) }
        confirmVerified(taskRepository)
    }
}