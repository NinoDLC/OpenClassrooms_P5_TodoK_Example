package fr.delcey.todok.domain.project

import fr.delcey.todok.domain.project.model.ProjectEntity
import javax.inject.Inject

class AddProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
) {
    suspend fun invoke(project: ProjectEntity.Simple) {
        projectRepository.add(project)
    }
}
