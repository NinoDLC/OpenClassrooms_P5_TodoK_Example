package fr.delcey.todok.domain.task

import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.data.dao.TaskDao
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

    private val taskDao: TaskDao = mockk()

    private val deleteTaskUseCase = DeleteTaskUseCase(taskDao)

    @Before
    fun setUp() {
        coJustRun { taskDao.delete(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // Given
        val taskId = 666L

        // When
        deleteTaskUseCase.invoke(taskId)

        // Then
        coVerify(exactly = 1) { taskDao.delete(taskId) }
        confirmVerified(taskDao)
    }
}