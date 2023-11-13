package fr.delcey.todok.domain.task

import fr.delcey.todok.domain.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddTaskUseCaseTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 3L
        private const val DEFAULT_TASK_DESCRIPTION = "DEFAULT_TASK_DESCRIPTION"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskRepository: TaskRepository = mockk()

    private val addTaskUseCase = AddTaskUseCase(taskRepository)

    @Before
    fun setUp() {
        coEvery { taskRepository.add(any(), any()) } returns true
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = addTaskUseCase.invoke(DEFAULT_PROJECT_ID, DEFAULT_TASK_DESCRIPTION)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { taskRepository.add(DEFAULT_PROJECT_ID, DEFAULT_TASK_DESCRIPTION) }
        confirmVerified(taskRepository)
    }

    @Test
    fun `error case`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskRepository.add(any(), any()) } returns false

        // When
        val result = addTaskUseCase.invoke(DEFAULT_PROJECT_ID, DEFAULT_TASK_DESCRIPTION)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { taskRepository.add(DEFAULT_PROJECT_ID, DEFAULT_TASK_DESCRIPTION) }
        confirmVerified(taskRepository)
    }
}