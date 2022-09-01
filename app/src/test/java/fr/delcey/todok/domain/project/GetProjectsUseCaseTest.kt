package fr.delcey.todok.domain.project

import assertk.assertThat
import assertk.assertions.isEqualTo
import fr.delcey.todok.data.dao.ProjectDao
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class GetProjectsUseCaseTest {

    companion object {
        private val GET_ALL_FLOW: Flow<List<ProjectEntity>> = flowOf()
    }

    private val projectDao: ProjectDao = mockk()

    private val getProjectsUseCase = GetProjectsUseCase(projectDao)

    @Before
    fun setUp() {
        every { projectDao.getAll() } returns GET_ALL_FLOW
    }

    @Test
    fun verify() {
        // When
        val result = getProjectsUseCase.invoke()

        // Then
        assertThat(result).isEqualTo(GET_ALL_FLOW)
        verify(exactly = 1) { projectDao.getAll() }
        confirmVerified(projectDao)
    }
}