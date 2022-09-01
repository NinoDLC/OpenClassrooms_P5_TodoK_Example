package fr.delcey.todok.domain.project

import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.data.dao.ProjectDao
import fr.delcey.todok.getDefaultProjectEntity
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertProjectUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val projectDao: ProjectDao = mockk()

    private val insertProjectUseCase = InsertProjectUseCase(projectDao)

    @Before
    fun setUp() {
        coJustRun { projectDao.insert(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // When
        insertProjectUseCase.invoke(getDefaultProjectEntity(0))

        // Then
        coVerify(exactly = 1) { projectDao.insert(getDefaultProjectEntity(0)) }
        confirmVerified(projectDao)
    }
}