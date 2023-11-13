package fr.delcey.todok.domain.task

import fr.delcey.todok.domain.config.ConfigResolver
import fr.delcey.todok.domain.project.GetProjectsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddRandomTaskUseCase @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val configResolver: ConfigResolver,
) {
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

    suspend fun invoke() {
        if (configResolver.isDebug) {
            val projectEntity = getProjectsUseCase.invoke().first().random()
            val description = DESCRIPTIONS.random()

            addTaskUseCase.invoke(
                projectId = projectEntity.id,
                description = description,
            )
        }
    }
}
