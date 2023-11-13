package fr.delcey.todok.domain.project_with_tasks

import fr.delcey.todok.domain.project.model.ProjectEntity
import fr.delcey.todok.domain.project.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProjectsWithTasksUseCase @Inject constructor(
    private val projectRepository: ProjectRepository,
) {
    fun invoke(): Flow<List<ProjectEntity.WithTasks>> = projectRepository.getProjectsWithTasksAsFlow()
}
