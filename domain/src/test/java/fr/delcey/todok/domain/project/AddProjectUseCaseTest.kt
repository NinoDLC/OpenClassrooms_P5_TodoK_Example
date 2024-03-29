package fr.delcey.todok.domain.project

import fr.delcey.todok.domain.TestCoroutineRule
import fr.delcey.todok.domain.getDefaultProjectEntitySimple
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddProjectUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val projectRepository: ProjectRepository = mockk()

    private val addProjectUseCase = AddProjectUseCase(projectRepository)

    @Before
    fun setUp() {
        coJustRun { projectRepository.add(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // When
        addProjectUseCase.invoke(getDefaultProjectEntitySimple(0))

        // Then
        coVerify(exactly = 1) { projectRepository.add(getDefaultProjectEntitySimple(0)) }
        confirmVerified(projectRepository)
    }
}