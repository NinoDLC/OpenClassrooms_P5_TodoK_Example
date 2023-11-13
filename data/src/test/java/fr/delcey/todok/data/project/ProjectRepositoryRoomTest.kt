package fr.delcey.todok.data.project

import app.cash.turbine.test
import fr.delcey.todok.data.TestCoroutineRule
import fr.delcey.todok.data.getDefaultProjectDto
import fr.delcey.todok.data.getDefaultProjectDtos
import fr.delcey.todok.data.getDefaultProjectEntitySimple
import fr.delcey.todok.data.getDefaultProjectEntitySimpleList
import fr.delcey.todok.data.getDefaultProjectEntityWithTasks
import fr.delcey.todok.data.getDefaultProjectEntityWithTasksList
import fr.delcey.todok.data.getDefaultProjectWithTasksDtos
import fr.delcey.todok.data.getDefaultTaskEntity
import fr.delcey.todok.data.project.model.ProjectDto
import fr.delcey.todok.data.task.TaskMapper
import fr.delcey.todok.data.task.model.TaskDto
import fr.delcey.todok.domain.task.model.TaskEntity
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProjectRepositoryRoomTest {

    companion object {
        private const val DEFAULT_PROJECT_ID = 6
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val projectDao: ProjectDao = mockk()
    private val projectMapper: ProjectMapper = mockk()
    private val taskMapper: TaskMapper = mockk()

    private val projectRepositoryRoom = ProjectRepositoryRoom(
        projectDao,
        projectMapper,
        taskMapper,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        every { projectMapper.map(getDefaultProjectEntitySimple(DEFAULT_PROJECT_ID)) } returns getDefaultProjectDto(DEFAULT_PROJECT_ID)
        coJustRun { projectDao.insert(getDefaultProjectDto(DEFAULT_PROJECT_ID)) }
        every { projectDao.getProjectsAsFlow() } returns flowOf(getDefaultProjectDtos(DEFAULT_PROJECT_ID))

        val projectDtoSlot = slot<ProjectDto>()
        every { projectMapper.map(capture(projectDtoSlot)) } answers {
            getDefaultProjectEntitySimple(projectDtoSlot.captured.id)
        }

        every { projectDao.getProjectsWithTasksAsFlow() } returns flowOf(getDefaultProjectWithTasksDtos(DEFAULT_PROJECT_ID))

        val taskDtoSlot = slot<TaskDto>()
        every { taskMapper.map(capture(taskDtoSlot)) } answers {
            getDefaultTaskEntity(projectId = taskDtoSlot.captured.projectId, taskId = taskDtoSlot.captured.id)
        }

        val taskEntitiesSlot = slot<List<TaskEntity>>()
        every { projectMapper.map(capture(projectDtoSlot), capture(taskEntitiesSlot)) } answers {
            getDefaultProjectEntityWithTasks(projectDtoSlot.captured.id).copy(
                tasks = taskEntitiesSlot.captured
            )
        }
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // When
        projectRepositoryRoom.add(getDefaultProjectEntitySimple(DEFAULT_PROJECT_ID))

        // Then
        coVerify(exactly = 1) { projectDao.insert(getDefaultProjectDto(DEFAULT_PROJECT_ID)) }
        confirmVerified(projectDao)
    }

    @Test
    fun `getProjectsAsFlow - nominal case`() = testCoroutineRule.runTest {
        // When
        projectRepositoryRoom.getProjectsAsFlow().test {
            val result = awaitItem()
            awaitComplete()

            // Then
            assertEquals(getDefaultProjectEntitySimpleList(DEFAULT_PROJECT_ID), result)
            coVerify(exactly = 1) { projectDao.getProjectsAsFlow() }
            confirmVerified(projectDao)
        }
    }

    @Test
    fun `getProjectsWithTasksAsFlow - nominal case`() = testCoroutineRule.runTest {
        // When
        projectRepositoryRoom.getProjectsWithTasksAsFlow().test {
            val result = awaitItem()
            awaitComplete()

            // Then
            assertEquals(getDefaultProjectEntityWithTasksList(DEFAULT_PROJECT_ID), result)
            coVerify(exactly = 1) { projectDao.getProjectsWithTasksAsFlow() }
            confirmVerified(projectDao)
        }
    }
}

