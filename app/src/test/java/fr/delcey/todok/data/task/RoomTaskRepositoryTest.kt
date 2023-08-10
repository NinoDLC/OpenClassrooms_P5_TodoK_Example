package fr.delcey.todok.data.task

import android.database.sqlite.SQLiteException
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.ensuresDispatcher
import fr.delcey.todok.getDefaultTaskEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoomTaskRepositoryTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 3L
        private const val DEFAULT_TASK_ID = 7L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskDao: TaskDao = mockk()

    private val roomTaskRepository = RoomTaskRepository(
        taskDao,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coEvery {
            taskDao.insert(
                getDefaultTaskEntity(
                    DEFAULT_PROJECT_ID,
                    DEFAULT_TASK_ID
                )
            )
        }.ensuresDispatcher(testCoroutineRule.ioDispatcher)
        coEvery { taskDao.delete(DEFAULT_TASK_ID) }.ensuresDispatcher(testCoroutineRule.ioDispatcher) { 1 }
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = roomTaskRepository.add(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID))

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID)) }
        confirmVerified(taskDao)
    }

    @Test
    fun `add - error case - SQLiteException is thrown`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskDao.insert(any()) } throws SQLiteException()

        // When
        val result = roomTaskRepository.add(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID))

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(DEFAULT_PROJECT_ID, DEFAULT_TASK_ID)) }
        confirmVerified(taskDao)
    }

    @Test
    fun `delete - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = roomTaskRepository.delete(DEFAULT_TASK_ID)

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { taskDao.delete(DEFAULT_TASK_ID) }
        confirmVerified(taskDao)
    }

    @Test
    fun `delete - error case`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskDao.delete(DEFAULT_TASK_ID) } returns 0

        // When
        val result = roomTaskRepository.delete(DEFAULT_TASK_ID)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskDao.delete(DEFAULT_TASK_ID) }
        confirmVerified(taskDao)
    }
}