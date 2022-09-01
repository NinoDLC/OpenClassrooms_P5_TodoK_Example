package fr.delcey.todok.domain.task

import android.database.sqlite.SQLiteException
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.data.dao.TaskDao
import fr.delcey.todok.getDefaultTaskEntity
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertTaskUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskDao: TaskDao = mockk()

    private val insertTaskUseCase = InsertTaskUseCase(taskDao)

    @Before
    fun setUp() {
        coJustRun { taskDao.insert(any()) }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = insertTaskUseCase.invoke(getDefaultTaskEntity(3, 7))

        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(3, 7)) }
        confirmVerified(taskDao)
    }

    @Test
    fun `error case - SQLiteException is thrown`() = testCoroutineRule.runTest {
        // Given
        coEvery { insertTaskUseCase.invoke(any()) } throws SQLiteException()

        // When
        val result = insertTaskUseCase.invoke(getDefaultTaskEntity(3, 7))

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(3, 7)) }
        confirmVerified(taskDao)
    }
}