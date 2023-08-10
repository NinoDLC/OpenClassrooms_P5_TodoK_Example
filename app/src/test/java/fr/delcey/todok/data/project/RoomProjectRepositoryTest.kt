package fr.delcey.todok.data.project

import app.cash.turbine.test
import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.domain.project.ProjectEntity
import fr.delcey.todok.domain.project_with_tasks.ProjectWithTasksEntity
import fr.delcey.todok.emptyFlowOnDispatcher
import fr.delcey.todok.ensuresDispatcher
import fr.delcey.todok.getDefaultProjectEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoomProjectRepositoryTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 6
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val projectDao: ProjectDao = mockk()

    private val defaultGetProjectsFlow: Flow<List<ProjectEntity>> = emptyFlowOnDispatcher(testCoroutineRule.ioDispatcher)
    private val defaultGetProjectsWithTasksFlow: Flow<List<ProjectWithTasksEntity>> = emptyFlowOnDispatcher(testCoroutineRule.ioDispatcher)

    private val roomProjectRepository = RoomProjectRepository(
        projectDao,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coEvery { projectDao.insert(getDefaultProjectEntity(DEFAULT_PROJECT_ID)) }.ensuresDispatcher(testCoroutineRule.ioDispatcher)
        every { projectDao.getProjectsAsFlow() } returns defaultGetProjectsFlow
        every { projectDao.getProjectsWithTasksAsFlow() } returns defaultGetProjectsWithTasksFlow
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // When
        roomProjectRepository.add(getDefaultProjectEntity(DEFAULT_PROJECT_ID))

        // Then
        coVerify(exactly = 1) { projectDao.insert(getDefaultProjectEntity(DEFAULT_PROJECT_ID)) }
        confirmVerified(projectDao)
    }

    @Test
    fun `getProjectsAsFlow - nominal case`() = testCoroutineRule.runTest {
        // When
        roomProjectRepository.getProjectsAsFlow().test {
            awaitComplete()
        }

        // Then
        coVerify(exactly = 1) { projectDao.getProjectsAsFlow() }
        confirmVerified(projectDao)
    }

    @Test
    fun `getProjectsWithTasksAsFlow - nominal case`() = testCoroutineRule.runTest {
        // When
        roomProjectRepository.getProjectsWithTasksAsFlow().test {
            awaitComplete()
        }

        // Then
        coVerify(exactly = 1) { projectDao.getProjectsWithTasksAsFlow() }
        confirmVerified(projectDao)
    }
}

