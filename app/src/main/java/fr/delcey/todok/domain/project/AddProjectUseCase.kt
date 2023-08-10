package fr.delcey.todok.domain.project

import javax.inject.Inject

class AddProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
) {
    suspend fun invoke(projectEntity: ProjectEntity) {
        projectRepository.add(projectEntity)
    }
}
