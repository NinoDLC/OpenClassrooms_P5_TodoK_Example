package fr.delcey.todok.domain.task

import fr.delcey.todok.TestCoroutineRule
import fr.delcey.todok.data.BuildConfigResolver
import fr.delcey.todok.domain.project.GetProjectsUseCase
import fr.delcey.todok.getDefaultProjectEntities
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddRandomTaskUseCaseTest {

    companion object {
        private val DESCRIPTIONS = listOf(
            "Nettoyer les vitres",
            "Vider le lave-vaisselle",
            "Passer l'aspirateur",
            "Arroser les plantes",
            "Nettoyer les toilettes",
            "Jouer avec le chat",
            "Préparer un diner romantique",
            "Étendre le linge",
            "Sortir le chien",
        )
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getProjectsUseCase: GetProjectsUseCase = mockk()
    private val addTaskUseCase: AddTaskUseCase = mockk()
    private val buildConfigResolver: BuildConfigResolver = mockk()

    private val addRandomTaskUseCase = AddRandomTaskUseCase(getProjectsUseCase, addTaskUseCase, buildConfigResolver)

    @Before
    fun setUp() {
        every { getProjectsUseCase.invoke() } returns flowOf(getDefaultProjectEntities())
        coEvery { addTaskUseCase.invoke(any()) } returns true
    }

    @Test
    fun `nominal case - debug`() = testCoroutineRule.runTest {
        // Given
        every { buildConfigResolver.isDebug } returns true

        // When
        addRandomTaskUseCase.invoke()

        // Then
        coVerify(exactly = 1) {
            getProjectsUseCase.invoke()
            addTaskUseCase.invoke(
                match { taskEntity ->
                    taskEntity.id == 0L
                        && getDefaultProjectEntities().map { it.id }.contains(taskEntity.projectId)
                        && DESCRIPTIONS.contains(taskEntity.description)
                }
            )
            buildConfigResolver.isDebug
        }
        confirmVerified(getProjectsUseCase, addTaskUseCase, buildConfigResolver)
    }

    @Test
    fun `nominal case - release`() = testCoroutineRule.runTest {
        // Given
        every { buildConfigResolver.isDebug } returns false

        // When
        addRandomTaskUseCase.invoke()

        // Then
        verify(exactly = 1) { buildConfigResolver.isDebug }
        confirmVerified(getProjectsUseCase, addTaskUseCase, buildConfigResolver)
    }
}