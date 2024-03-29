package fr.delcey.todok.domain.project

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

class GetProjectsUseCaseTest {

    companion object {
        private val GET_ALL_FLOW: Flow<List<ProjectEntity.Simple>> = flowOf()
    }

    private val projectRepository: ProjectRepository = mockk()

    private val getProjectsUseCase = GetProjectsUseCase(projectRepository)

    @Before
    fun setUp() {
        every { projectRepository.getProjectsAsFlow() } returns GET_ALL_FLOW
    }

    @Test
    fun verify() {
        // When
        val result = getProjectsUseCase.invoke()

        // Then
        assertEquals(GET_ALL_FLOW, result)
        verify(exactly = 1) { projectRepository.getProjectsAsFlow() }
        confirmVerified(projectRepository)
    }
}