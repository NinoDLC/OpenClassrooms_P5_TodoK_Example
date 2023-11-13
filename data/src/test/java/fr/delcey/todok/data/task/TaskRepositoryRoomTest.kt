package fr.delcey.todok.data.task

import android.database.sqlite.SQLiteException
import fr.delcey.todok.data.TestCoroutineRule
import fr.delcey.todok.data.getDefaultTaskDto
import fr.delcey.todok.data.getDefaultTaskDtoDescription
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskRepositoryRoomTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 3L
        private const val DEFAULT_TASK_ID = 7L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskDao: TaskDao = mockk()

    private val taskRepositoryRoom = TaskRepositoryRoom(
        taskDao,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coJustRun {
            taskDao.insert(
                getDefaultTaskDto(
                    taskId = 0,
                    projectId = DEFAULT_PROJECT_ID,
                )
            )
        }
        coEvery { taskDao.delete(DEFAULT_TASK_ID) } returns 1
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = taskRepositoryRoom.add(
            projectId = DEFAULT_PROJECT_ID,
            description = getDefaultTaskDtoDescription(projectId = DEFAULT_PROJECT_ID, taskId = 0)
        )

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskDto(taskId = 0, projectId = DEFAULT_PROJECT_ID)) }
        confirmVerified(taskDao)
    }

    @Test
    fun `add - error case - SQLiteException is thrown`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskDao.insert(any()) } throws SQLiteException()

        // When
        val result = taskRepositoryRoom.add(
            projectId = DEFAULT_PROJECT_ID,
            description = getDefaultTaskDtoDescription(projectId = DEFAULT_PROJECT_ID, taskId = 0)
        )

        // Then
        assertFalse(result)
    }

    @Test
    fun `delete - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = taskRepositoryRoom.delete(DEFAULT_TASK_ID)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { taskDao.delete(DEFAULT_TASK_ID) }
        confirmVerified(taskDao)
    }

    @Test
    fun `delete - error case`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskDao.delete(DEFAULT_TASK_ID) } returns 0

        // When
        val result = taskRepositoryRoom.delete(DEFAULT_TASK_ID)

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { taskDao.delete(DEFAULT_TASK_ID) }
        confirmVerified(taskDao)
    }
}