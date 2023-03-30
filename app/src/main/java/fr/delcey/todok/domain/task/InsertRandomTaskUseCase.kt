package fr.delcey.todok.domain.task

import fr.delcey.todok.data.BuildConfigResolver
import fr.delcey.todok.domain.project.GetProjectsUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class InsertRandomTaskUseCase @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val buildConfigResolver: BuildConfigResolver,
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
        if (buildConfigResolver.isDebug) {
            val projectEntity = getProjectsUseCase.invoke().first().random()
            val description = DESCRIPTIONS.random()

            insertTaskUseCase.invoke(
                TaskEntity(
                    projectId = projectEntity.id,
                    description = description,
                )
            )
        }
    }
}
