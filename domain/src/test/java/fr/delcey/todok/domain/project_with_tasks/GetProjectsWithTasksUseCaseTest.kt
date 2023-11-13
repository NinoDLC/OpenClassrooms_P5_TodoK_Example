package fr.delcey.todok.domain.project_with_tasks

import fr.delcey.todok.domain.project.ProjectRepository
import fr.delcey.todok.domain.project.model.ProjectEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProjectsWithTasksUseCaseTest {

    companion object {
        private val GET_ALL_PROJECTS_WITH_TASKS_FLOW: Flow<List<ProjectEntity.WithTasks>> = flowOf()
    }

    private val projectRepository: ProjectRepository = mockk()

    private val getProjectsWithTasksUseCase = GetProjectsWithTasksUseCase(projectRepository)

    @Before
    fun setUp() {
        every { projectRepository.getProjectsWithTasksAsFlow() } returns GET_ALL_PROJECTS_WITH_TASKS_FLOW
    }

    @Test
    fun verify() {
        // When
        val result = getProjectsWithTasksUseCase.invoke()

        // Then
        assertEquals(GET_ALL_PROJECTS_WITH_TASKS_FLOW, result)
        verify(exactly = 1) { projectRepository.getProjectsWithTasksAsFlow() }
        confirmVerified(projectRepository)
    }
}