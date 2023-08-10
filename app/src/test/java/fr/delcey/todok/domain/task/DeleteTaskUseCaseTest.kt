package fr.delcey.todok.domain.task

import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.data.task.TaskDao
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteTaskUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskRepository: TaskRepository = mockk()

    private val deleteTaskUseCase = DeleteTaskUseCase(taskRepository)

    @Before
    fun setUp() {
        coJustRun { taskRepository.delete(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // Given
        val taskId = 666L

        // When
        deleteTaskUseCase.invoke(taskId)

        // Then
        coVerify(exactly = 1) { taskRepository.delete(taskId) }
        confirmVerified(taskRepository)
    }
}